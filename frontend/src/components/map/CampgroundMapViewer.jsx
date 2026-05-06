
// ============================================================
// Fichier : frontend/src/components/map/CampgroundMapViewer.jsx
// Dernière modification : 2026-05-05
//
// Résumé :
// - Zoom/pan fluide
// - Coordonnées indépendantes du zoom
// - Snap précis aux 1 pixel
// - Sélection visuelle d’un site plus claire et subtile
// - Popup fixe au clic avec bouton fermer
// - Hover seulement visuel, sans popup qui suit la souris
// - Double-clic sur site = modifier
// - Mode édition : ajout, déplacement et suppression de points
// - Traits de sélection et de dessin moins épais
// - Correction du tri naturel des codes de site : 1,2,10 / A101,A102
// ============================================================

import React, { useEffect, useMemo, useRef, useState } from "react";
import {
  CalendarCheck,
  Edit3,
  Minus,
  Move,
  Plus,
  Tent,
  X,
} from "lucide-react";

const SNAP_SIZE = 1;

function snap(value) {
  return Math.round(value / SNAP_SIZE) * SNAP_SIZE;
}

function sortSitesByCode(sites) {
  return [...(Array.isArray(sites) ? sites : [])].sort((a, b) =>
    String(a?.siteCode ?? "").localeCompare(
      String(b?.siteCode ?? ""),
      "fr-CA",
      {
        numeric: true,
        sensitivity: "base",
      }
    )
  );
}

function pointsToSvg(points) {
  if (!Array.isArray(points)) return "";
  return points
    .filter((p) => p && Number.isFinite(p.x) && Number.isFinite(p.y))
    .map((p) => `${p.x},${p.y}`)
    .join(" ");
}

function clamp(value, min, max) {
  return Math.min(Math.max(value, min), max);
}

function getPolygonCenter(points) {
  if (!Array.isArray(points) || points.length === 0) return null;

  const validPoints = points.filter(
    (p) => p && Number.isFinite(p.x) && Number.isFinite(p.y)
  );

  if (validPoints.length === 0) return null;

  const sum = validPoints.reduce(
    (acc, p) => ({ x: acc.x + p.x, y: acc.y + p.y }),
    { x: 0, y: 0 }
  );

  return {
    x: sum.x / validPoints.length,
    y: sum.y / validPoints.length,
  };
}

export default function CampgroundMapViewer({
  mapData,
  selectedSiteId,
  draftPoints = [],
  editMode = false,
  onSiteClick,
  onElementClick,
  onAddPoint,
  onUpdateDraftPoint,
  onDeleteDraftPoint,
  onEditSite,
  onReserveSite,
}) {
  const imageWidth = mapData?.imageWidth || 1600;
  const imageHeight = mapData?.imageHeight || 900;
  const backgroundImagePath = mapData?.backgroundImagePath || "";
  const sites = Array.isArray(mapData?.sites) ? mapData.sites : [];
  const sortedSites = useMemo(() => sortSitesByCode(sites), [sites]);
  const elements = Array.isArray(mapData?.elements) ? mapData.elements : [];

  const viewportRef = useRef(null);

  const [scale, setScale] = useState(1);
  const [pan, setPan] = useState({ x: 0, y: 0 });
  const [isPanning, setIsPanning] = useState(false);
  const [spacePressed, setSpacePressed] = useState(false);
  const [draggingPointIndex, setDraggingPointIndex] = useState(null);
  const [hoveredSite, setHoveredSite] = useState(null);
  const [hoveredElement, setHoveredElement] = useState(null);
  const [popupSite, setPopupSite] = useState(null);
  const [popupElement, setPopupElement] = useState(null);

  const [panStart, setPanStart] = useState({
    mouseX: 0,
    mouseY: 0,
    panX: 0,
    panY: 0,
  });

  const selectedSite = useMemo(
    () => sites.find((site) => site.id === selectedSiteId) || null,
    [sites, selectedSiteId]
  );

  const clampPan = (nextPanX, nextPanY, nextScale = scale) => {
    const viewport = viewportRef.current;
    if (!viewport) return { x: nextPanX, y: nextPanY };

    const rect = viewport.getBoundingClientRect();

    const scaledWidth = rect.width * nextScale;
    const scaledHeight = rect.height * nextScale;

    const visibleMargin = 120;

    let minPanX = rect.width - scaledWidth - visibleMargin;
    let maxPanX = visibleMargin;

    let minPanY = rect.height - scaledHeight - visibleMargin;
    let maxPanY = visibleMargin;

    if (scaledWidth <= rect.width) {
      minPanX = maxPanX = (rect.width - scaledWidth) / 2;
    }

    if (scaledHeight <= rect.height) {
      minPanY = maxPanY = (rect.height - scaledHeight) / 2;
    }

    return {
      x: clamp(nextPanX, minPanX, maxPanX),
      y: clamp(nextPanY, minPanY, maxPanY),
    };
  };

  const getWorldPointFromMouseEvent = (e) => {
    const viewport = viewportRef.current;
    if (!viewport) return { x: 0, y: 0 };

    const rect = viewport.getBoundingClientRect();

    const localX = (e.clientX - rect.left - pan.x) / scale;
    const localY = (e.clientY - rect.top - pan.y) / scale;

    const worldX = (localX / rect.width) * imageWidth;
    const worldY = (localY / rect.height) * imageHeight;

    return {
      x: snap(worldX),
      y: snap(worldY),
    };
  };

  useEffect(() => {
    const handleKeyDown = (e) => {
      if (e.code !== "Space") return;

      const target = e.target;
      const isTypingTarget =
        target instanceof HTMLElement &&
        (target.tagName === "INPUT" ||
          target.tagName === "TEXTAREA" ||
          target.isContentEditable);

      if (!isTypingTarget) {
        e.preventDefault();
        setSpacePressed(true);
      }
    };

    const handleKeyUp = (e) => {
      if (e.code === "Space") {
        setSpacePressed(false);
        setIsPanning(false);
      }
    };

    window.addEventListener("keydown", handleKeyDown);
    window.addEventListener("keyup", handleKeyUp);

    return () => {
      window.removeEventListener("keydown", handleKeyDown);
      window.removeEventListener("keyup", handleKeyUp);
    };
  }, []);

  const zoomAtPoint = (nextScale, clientX, clientY) => {
    const viewport = viewportRef.current;
    if (!viewport) return;

    const rect = viewport.getBoundingClientRect();

    const pointerX = clientX - rect.left;
    const pointerY = clientY - rect.top;

    const worldX = (pointerX - pan.x) / scale;
    const worldY = (pointerY - pan.y) / scale;

    const unclampedPan = {
      x: pointerX - worldX * nextScale,
      y: pointerY - worldY * nextScale,
    };

    const boundedPan = clampPan(unclampedPan.x, unclampedPan.y, nextScale);

    setPan(boundedPan);
    setScale(nextScale);
  };

  const centerOnWorldPoint = (worldX, worldY, nextScale = scale) => {
    const viewport = viewportRef.current;
    if (!viewport) return;

    const rect = viewport.getBoundingClientRect();

    const screenX = (worldX / imageWidth) * rect.width;
    const screenY = (worldY / imageHeight) * rect.height;

    const unclampedPan = {
      x: rect.width / 2 - screenX * nextScale,
      y: rect.height / 2 - screenY * nextScale,
    };

    const boundedPan = clampPan(unclampedPan.x, unclampedPan.y, nextScale);

    setScale(nextScale);
    setPan(boundedPan);
    setIsPanning(false);
  };

  useEffect(() => {
    if (!selectedSite) return;

    const center =
      Number.isFinite(selectedSite.labelX) && Number.isFinite(selectedSite.labelY)
        ? { x: selectedSite.labelX, y: selectedSite.labelY }
        : getPolygonCenter(selectedSite.polygon);

    if (!center) return;

    centerOnWorldPoint(center.x, center.y, 1.5);
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [selectedSiteId]);

  useEffect(() => {
    const boundedPan = clampPan(pan.x, pan.y, scale);

    if (boundedPan.x !== pan.x || boundedPan.y !== pan.y) {
      setPan(boundedPan);
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [scale, imageWidth, imageHeight]);

  const handleWheel = (e) => {
    if (!backgroundImagePath) return;

    e.preventDefault();

    const factor = e.deltaY < 0 ? 1.12 : 0.88;
    const nextScale = clamp(scale * factor, 0.5, 4);

    if (nextScale === scale) return;

    zoomAtPoint(nextScale, e.clientX, e.clientY);
  };

  const handleMouseDown = (e) => {
    const shouldPan = e.button === 1 || spacePressed;

    if (!shouldPan || draggingPointIndex !== null) return;

    e.preventDefault();

    setIsPanning(true);
    setPanStart({
      mouseX: e.clientX,
      mouseY: e.clientY,
      panX: pan.x,
      panY: pan.y,
    });
  };

  const handleMouseMove = (e) => {
    if (!isPanning) return;

    e.preventDefault();

    const unclampedPan = {
      x: panStart.panX + (e.clientX - panStart.mouseX),
      y: panStart.panY + (e.clientY - panStart.mouseY),
    };

    setPan(clampPan(unclampedPan.x, unclampedPan.y, scale));
  };

  const stopPan = () => {
    setIsPanning(false);
  };

  const zoomIn = () => {
    const viewport = viewportRef.current;
    if (!viewport) return;

    const rect = viewport.getBoundingClientRect();
    const nextScale = clamp(scale * 1.15, 0.5, 4);

    zoomAtPoint(
      nextScale,
      rect.left + rect.width / 2,
      rect.top + rect.height / 2
    );
  };

  const zoomOut = () => {
    const viewport = viewportRef.current;
    if (!viewport) return;

    const rect = viewport.getBoundingClientRect();
    const nextScale = clamp(scale / 1.15, 0.5, 4);

    zoomAtPoint(
      nextScale,
      rect.left + rect.width / 2,
      rect.top + rect.height / 2
    );
  };

  const resetView = () => {
    setScale(1);
    setPan(clampPan(0, 0, 1));
    setIsPanning(false);
  };

  const handleSvgClick = (e) => {
    if (!editMode || !onAddPoint || isPanning || draggingPointIndex !== null) {
      return;
    }

    const point = getWorldPointFromMouseEvent(e);
    onAddPoint(point);
  };

  const handleDraftPointMouseDown = (e, index) => {
    if (!editMode || !onUpdateDraftPoint) return;

    e.preventDefault();
    e.stopPropagation();

    setDraggingPointIndex(index);

    const handleMove = (moveEvent) => {
      moveEvent.preventDefault();

      const point = getWorldPointFromMouseEvent(moveEvent);
      onUpdateDraftPoint(index, point);
    };

    const handleUp = () => {
      setDraggingPointIndex(null);
      window.removeEventListener("mousemove", handleMove);
      window.removeEventListener("mouseup", handleUp);
    };

    window.addEventListener("mousemove", handleMove);
    window.addEventListener("mouseup", handleUp);
  };

  const closePopup = () => {
    setPopupSite(null);
    setPopupElement(null);
  };

  const draftCenter =
    draftPoints.length > 0
      ? {
          x: Math.round(
            draftPoints.reduce((sum, p) => sum + p.x, 0) / draftPoints.length
          ),
          y: Math.round(
            draftPoints.reduce((sum, p) => sum + p.y, 0) / draftPoints.length
          ),
        }
      : null;

  const cursorClass = isPanning
    ? "cursor-grabbing"
    : spacePressed
    ? "cursor-grab"
    : editMode
    ? "cursor-crosshair"
    : "cursor-default";

  return (
    <div className="bg-white rounded-3xl shadow-sm border p-4">
      <div className="rounded-2xl border bg-slate-100 p-3">
        <div className="mb-3 flex flex-wrap items-center justify-between gap-3">
          <div>
            <p className="text-sm font-semibold text-slate-900">
              Carte du camping
            </p>
            <p className="text-xs text-slate-500">
              Roulette = zoom • Espace + glisser = déplacer • Zoom actuel :{" "}
              {Math.round(scale * 100)}%
              {editMode
                ? " • Clic = ajouter un point • Glisser un point rouge = modifier • Double-clic = supprimer"
                : " • Clic sur un site = sélectionner"}
            </p>
          </div>

          <div className="flex flex-wrap items-center gap-2">
            {selectedSite && onEditSite && (
              <button
                type="button"
                onClick={() => onEditSite(selectedSite)}
                className="inline-flex items-center gap-2 rounded-xl border bg-white px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
              >
                <Edit3 className="h-4 w-4" />
                Modifier
              </button>
            )}

            {selectedSite && onReserveSite && (
              <button
                type="button"
                onClick={() => onReserveSite(selectedSite)}
                className="inline-flex items-center gap-2 rounded-xl bg-orange-600 px-3 py-2 text-sm font-medium text-white hover:bg-orange-700"
              >
                <CalendarCheck className="h-4 w-4" />
                Réserver
              </button>
            )}

            <button
              type="button"
              onClick={zoomOut}
              className="inline-flex h-10 w-10 items-center justify-center rounded-xl border bg-white text-slate-700 hover:bg-slate-50"
              title="Réduire"
            >
              <Minus className="h-4 w-4" />
            </button>

            <button
              type="button"
              onClick={resetView}
              className="inline-flex items-center gap-2 rounded-xl border bg-white px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
              title="Réinitialiser la vue"
            >
              <Move className="h-4 w-4" />
              100%
            </button>

            <button
              type="button"
              onClick={zoomIn}
              className="inline-flex h-10 w-10 items-center justify-center rounded-xl border bg-white text-slate-700 hover:bg-slate-50"
              title="Agrandir"
            >
              <Plus className="h-4 w-4" />
            </button>
          </div>
        </div>

        <div
          ref={viewportRef}
          className={`relative w-full overflow-hidden rounded-xl bg-slate-200 select-none touch-none ${cursorClass}`}
          style={{ aspectRatio: `${imageWidth} / ${imageHeight}` }}
          onWheel={handleWheel}
          onMouseDown={handleMouseDown}
          onMouseMove={handleMouseMove}
          onMouseUp={stopPan}
          onMouseLeave={() => {
            stopPan();
            setHoveredSite(null);
            setHoveredElement(null);
          }}
          onContextMenu={(e) => e.preventDefault()}
        >
          {backgroundImagePath ? (
            <div
              className="absolute inset-0"
              style={{
                transform: `translate(${pan.x}px, ${pan.y}px) scale(${scale})`,
                transformOrigin: "top left",
              }}
            >
              <img
                src={backgroundImagePath}
                alt="Plan du camping"
                className="absolute inset-0 block h-full w-full pointer-events-none object-fill"
                draggable={false}
              />

              <svg
                viewBox={`0 0 ${imageWidth} ${imageHeight}`}
                className="absolute inset-0 h-full w-full"
                preserveAspectRatio="none"
                onClick={handleSvgClick}
              >
                {elements.map((element) => {
                  const points = pointsToSvg(element.polygon);
                  if (!points) return null;

                  const isHovered = hoveredElement?.id === element.id;
                  const isPopupOpen = popupElement?.id === element.id;

                  return (
                    <g key={`element-${element.id}`}>
                      <polygon
                        points={points}
                        className={`cursor-pointer stroke-[1.5] ${
                          isPopupOpen
                            ? "fill-emerald-400/45 stroke-emerald-800"
                            : isHovered
                            ? "fill-emerald-400/35 stroke-emerald-700"
                            : element.isActive
                            ? "fill-emerald-300/25 stroke-emerald-700 hover:fill-emerald-400/35"
                            : "fill-slate-300/25 stroke-slate-600 hover:fill-slate-400/30"
                        }`}
                        onMouseEnter={() => setHoveredElement(element)}
                        onMouseLeave={() => setHoveredElement(null)}
                        onClick={(e) => {
                          e.stopPropagation();
                          setPopupElement(element);
                          setPopupSite(null);
                          onElementClick?.(element);
                        }}
                      />

                      {Number.isFinite(element.labelX) &&
                        Number.isFinite(element.labelY) && (
                          <text
                            x={element.labelX}
                            y={element.labelY}
                            textAnchor="middle"
                            dominantBaseline="middle"
                            className="fill-slate-900 text-[14px] font-semibold pointer-events-none"
                          >
                            {element.name}
                          </text>
                        )}
                    </g>
                  );
                })}

                {sortedSites.map((site) => {
                  const points = pointsToSvg(site.polygon);
                  if (!points) return null;

                  const isSelected = selectedSiteId === site.id;
                  const isHovered = hoveredSite?.id === site.id;
                  const isPopupOpen = popupSite?.id === site.id;

                  return (
                    <g key={`site-${site.id}`}>
                      <polygon
                        points={points}
                        className={`cursor-pointer transition-all ${
                          isSelected || isPopupOpen
                            ? "fill-sky-300/35 stroke-sky-700 stroke-[1.5]"
                            : isHovered
                            ? "fill-orange-300/35 stroke-orange-700 stroke-[1.25]"
                            : site.isActive
                            ? "fill-blue-300/25 stroke-blue-700 hover:fill-blue-400/35 stroke-[1.15]"
                            : "fill-slate-300/30 stroke-slate-700 hover:fill-slate-400/35 stroke-[1.15]"
                        }`}
                        onMouseEnter={() => setHoveredSite(site)}
                        onMouseLeave={() => setHoveredSite(null)}
                        onClick={(e) => {
                          e.stopPropagation();
                          setPopupSite(site);
                          setPopupElement(null);
                          onSiteClick?.(site);
                        }}
                        onDoubleClick={(e) => {
                          e.stopPropagation();
                          onEditSite?.(site);
                        }}
                      />

                      {(isSelected || isPopupOpen) && (
                        <polygon
                          points={points}
                          fill="rgba(125, 211, 252, 0.16)"
                          stroke="#0284c7"
                          strokeWidth="2"
                          pointerEvents="none"
                        />
                      )}

                      {Number.isFinite(site.labelX) &&
                        Number.isFinite(site.labelY) && (
                          <text
                            x={site.labelX}
                            y={site.labelY}
                            textAnchor="middle"
                            dominantBaseline="middle"
                            className="fill-slate-950 text-[14px] font-bold pointer-events-none"
                          >
                            {site.siteCode}
                          </text>
                        )}
                    </g>
                  );
                })}

                {draftPoints.length > 0 && (
                  <>
                    <polyline
                      points={pointsToSvg(draftPoints)}
                      fill="none"
                      stroke="#ef4444"
                      strokeWidth="1.5"
                      pointerEvents="none"
                    />

                    {draftPoints.length >= 3 && (
                      <polygon
                        points={pointsToSvg(draftPoints)}
                        fill="rgba(239,68,68,0.14)"
                        stroke="#ef4444"
                        strokeWidth="1.5"
                        pointerEvents="none"
                      />
                    )}

                    {draftPoints.map((point, index) => (
                      <g key={`draft-point-${index}`}>
                        <circle
                          cx={point.x}
                          cy={point.y}
                          r="6"
                          fill="#ef4444"
                          stroke="#ffffff"
                          strokeWidth="1.5"
                          className="cursor-move"
                          onMouseDown={(e) =>
                            handleDraftPointMouseDown(e, index)
                          }
                          onDoubleClick={(e) => {
                            e.preventDefault();
                            e.stopPropagation();
                            onDeleteDraftPoint?.(index);
                          }}
                        />

                        <text
                          x={point.x}
                          y={point.y - 12}
                          textAnchor="middle"
                          className="fill-red-700 text-[11px] font-bold pointer-events-none"
                        >
                          {index + 1}
                        </text>
                      </g>
                    ))}

                    {draftCenter && (
                      <text
                        x={draftCenter.x}
                        y={draftCenter.y}
                        textAnchor="middle"
                        dominantBaseline="middle"
                        className="fill-red-800 text-[14px] font-extrabold pointer-events-none"
                      >
                        Brouillon
                      </text>
                    )}
                  </>
                )}
              </svg>
            </div>
          ) : (
            <div className="flex min-h-[500px] items-center justify-center text-sm text-slate-500">
              Aucune image de fond configurée pour ce camping.
            </div>
          )}

          {(popupSite || popupElement) && (
            <div className="absolute right-4 top-4 z-50 w-80 max-w-[calc(100%-2rem)] rounded-2xl border bg-white shadow-xl">
              <div className="flex items-start justify-between gap-3 border-b px-4 py-3">
                <div>
                  <p className="text-sm font-bold text-slate-900">
                    {popupSite
                      ? `Site ${popupSite.siteCode || popupSite.id}`
                      : popupElement?.name || "Élément"}
                  </p>
                  <p className="text-xs text-slate-500">
                    Détails de la sélection
                  </p>
                </div>

                <button
                  type="button"
                  onClick={closePopup}
                  className="rounded-lg p-1 text-slate-500 hover:bg-slate-100 hover:text-slate-900"
                  title="Fermer"
                >
                  <X className="h-5 w-5" />
                </button>
              </div>

              <div className="space-y-2 px-4 py-3 text-sm text-slate-700">
                {popupSite && (
                  <>
                    <div className="flex items-center gap-2 font-semibold text-slate-900">
                      <Tent className="h-4 w-4 text-orange-600" />
                      Site {popupSite.siteCode || popupSite.id}
                    </div>

                    <p>
                      Statut :{" "}
                      <span
                        className={
                          popupSite.isActive
                            ? "font-semibold text-green-700"
                            : "font-semibold text-red-700"
                        }
                      >
                        {popupSite.isActive ? "Actif" : "Inactif"}
                      </span>
                    </p>

                    {popupSite.siteTypeName && (
                      <p>Type : {popupSite.siteTypeName}</p>
                    )}

                    {popupSite.serviceTypeName && (
                      <p>Services : {popupSite.serviceTypeName}</p>
                    )}

                    {popupSite.amperageName && (
                      <p>Ampérage : {popupSite.amperageName}</p>
                    )}

                    {Number.isFinite(popupSite.maxEquipmentLengthFeet) && (
                      <p>Longueur max : {popupSite.maxEquipmentLengthFeet} pi</p>
                    )}

                    <div className="flex flex-wrap gap-2 pt-2">
                      {onEditSite && (
                        <button
                          type="button"
                          onClick={() => onEditSite(popupSite)}
                          className="inline-flex items-center gap-2 rounded-xl border bg-white px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
                        >
                          <Edit3 className="h-4 w-4" />
                          Modifier
                        </button>
                      )}

                      {onReserveSite && (
                        <button
                          type="button"
                          onClick={() => onReserveSite(popupSite)}
                          className="inline-flex items-center gap-2 rounded-xl bg-orange-600 px-3 py-2 text-sm font-medium text-white hover:bg-orange-700"
                        >
                          <CalendarCheck className="h-4 w-4" />
                          Réserver
                        </button>
                      )}
                    </div>
                  </>
                )}

                {popupElement && (
                  <p>
                    Statut :{" "}
                    <span
                      className={
                        popupElement.isActive
                          ? "font-semibold text-green-700"
                          : "font-semibold text-red-700"
                      }
                    >
                      {popupElement.isActive ? "Actif" : "Inactif"}
                    </span>
                  </p>
                )}
              </div>
            </div>
          )}
        </div>

        {selectedSite && (
          <div className="mt-3 rounded-xl border bg-white p-3">
            <div className="flex flex-wrap items-center justify-between gap-3">
              <div>
                <p className="text-sm font-bold text-slate-900">
                  Site sélectionné : {selectedSite.siteCode || selectedSite.id}
                </p>
                <p className="text-xs text-slate-500">
                  Ce panneau est prêt pour l’édition et le futur module de
                  réservation.
                </p>
              </div>

              <div className="flex flex-wrap gap-2">
                {onEditSite && (
                  <button
                    type="button"
                    onClick={() => onEditSite(selectedSite)}
                    className="inline-flex items-center gap-2 rounded-xl border bg-white px-3 py-2 text-sm font-medium text-slate-700 hover:bg-slate-50"
                  >
                    <Edit3 className="h-4 w-4" />
                    Modifier ce site
                  </button>
                )}

                {onReserveSite && (
                  <button
                    type="button"
                    onClick={() => onReserveSite(selectedSite)}
                    className="inline-flex items-center gap-2 rounded-xl bg-orange-600 px-3 py-2 text-sm font-medium text-white hover:bg-orange-700"
                  >
                    <CalendarCheck className="h-4 w-4" />
                    Réserver ce site
                  </button>
                )}
              </div>
            </div>
          </div>
        )}
      </div>
    </div>
  );
}