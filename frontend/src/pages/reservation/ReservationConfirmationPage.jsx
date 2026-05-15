// ============================================================
// Fichier : ReservationConfirmationPage.jsx
// Chemin  : frontend/src/pages/reservation
// Dernière modification : 2026-05-14
// Auteur : ChatGPT pour Eric Beaudoin
//
// Résumé :
// - Page de confirmation avant paiement
// - Lit le terrain sélectionné depuis sessionStorage
// - Lit les critères de recherche depuis sessionStorage
// - Affiche les informations du séjour
// - Affiche les informations client depuis AuthContext
// - Affiche les informations équipement / critères
// - Affiche les détails du terrain
// - Calcule le prix réel avec /api/pricing-engine/calculate
// - Affiche le détail par nuit et le total
// - Affiche des badges de validation
// - Prépare le bouton de passage au paiement
//
// Historique des modifications :
// 2026-05-14
// - Création initiale de la page de confirmation
// - Ajout calcul prix réel via PricingEngineController
// - Ajout badges de validation avant paiement
// - Ajout affichage informations client
// - Ajout affichage critères équipement/recherche
// - Correction affichage dates et nombre de nuits
// - Bonification du résumé des coûts
// ============================================================

import React, { useEffect, useMemo, useState } from "react";
import {
  ArrowLeft,
  CalendarDays,
  CheckCircle2,
  CreditCard,
  Info,
  ShieldCheck,
  Tent,
  UserRound,
  XCircle,
} from "lucide-react";
import { useNavigate } from "react-router-dom";

import api from "../../services/api";
import { useAuth } from "../../context/AuthContext";

const SEARCH_SUMMARY_KEY = "gocamp_search_summary";
const SELECTED_CAMPSITE_KEY = "gocamp_selected_campsite";

function formatDate(value) {
  if (!value) {
    return "Non spécifiée";
  }

  return new Date(`${value}T00:00:00`).toLocaleDateString("fr-CA", {
    year: "numeric",
    month: "long",
    day: "numeric",
  });
}

function formatMoney(value) {
  if (value === null || value === undefined || Number.isNaN(Number(value))) {
    return "À confirmer";
  }

  return Number(value).toLocaleString("fr-CA", {
    style: "currency",
    currency: "CAD",
  });
}

function formatFeet(value) {
  if (value === null || value === undefined || value === "") {
    return "N/D";
  }

  return `${value} pi`;
}

function formatSurfaceValues(surfaceValues) {
  if (!surfaceValues) {
    return "Non spécifiée";
  }

  return String(surfaceValues)
    .split(";;")
    .map((item) => {
      const parts = item.split("|");
      return parts[1] || parts[0];
    })
    .filter(Boolean)
    .join(", ");
}

function calculateNights(arrivalDate, departureDate) {
  if (!arrivalDate || !departureDate) {
    return null;
  }

  const start = new Date(`${arrivalDate}T00:00:00`);
  const end = new Date(`${departureDate}T00:00:00`);
  const diffMs = end.getTime() - start.getTime();
  const nights = Math.round(diffMs / (1000 * 60 * 60 * 24));

  return nights > 0 ? nights : null;
}

function getFirstValue(...values) {
  return values.find(
    (value) => value !== null && value !== undefined && value !== ""
  );
}

function buildFullName(user) {
  const firstName = getFirstValue(
    user?.firstName,
    user?.firstname,
    user?.prenom,
    user?.first_name
  );

  const lastName = getFirstValue(
    user?.lastName,
    user?.lastname,
    user?.nom,
    user?.last_name
  );

  const fullName = `${firstName || ""} ${lastName || ""}`.trim();

  return fullName || getFirstValue(user?.name, user?.fullName) || "Non spécifié";
}

export default function ReservationConfirmationPage() {
  const navigate = useNavigate();
  const { user } = useAuth();

  const [selectedCampsite, setSelectedCampsite] = useState(null);
  const [searchSummary, setSearchSummary] = useState(null);

  const [priceResult, setPriceResult] = useState(null);
  const [priceLoading, setPriceLoading] = useState(false);
  const [priceError, setPriceError] = useState("");

  useEffect(() => {
    try {
      const rawSelected = sessionStorage.getItem(SELECTED_CAMPSITE_KEY);
      const rawSummary = sessionStorage.getItem(SEARCH_SUMMARY_KEY);

      setSelectedCampsite(rawSelected ? JSON.parse(rawSelected) : null);
      setSearchSummary(rawSummary ? JSON.parse(rawSummary) : null);
    } catch (err) {
      console.error("Erreur lecture sessionStorage confirmation :", err);
      setSelectedCampsite(null);
      setSearchSummary(null);
    }
  }, []);

  const searchCriteria =
    searchSummary?.searchCriteria ||
    searchSummary?.request ||
    searchSummary?.searchRequest ||
    {};

  const arrivalDate = getFirstValue(
    searchSummary?.arrivalDate,
    searchCriteria?.arrivalDate
  );

  const departureDate = getFirstValue(
    searchSummary?.departureDate,
    searchCriteria?.departureDate
  );

  const numberOfNights = useMemo(() => {
    return (
      searchSummary?.nights ||
      searchCriteria?.nights ||
      calculateNights(arrivalDate, departureDate)
    );
  }, [searchSummary, searchCriteria, arrivalDate, departureDate]);

  const requestedEquipmentLength = getFirstValue(
    searchCriteria?.equipmentLengthFeet,
    searchSummary?.equipmentLengthFeet
  );

  const isLengthCompatible = useMemo(() => {
    if (!requestedEquipmentLength || !selectedCampsite?.maxEquipmentLengthFeet) {
      return true;
    }

    return (
      Number(selectedCampsite.maxEquipmentLengthFeet) >=
      Number(requestedEquipmentLength)
    );
  }, [requestedEquipmentLength, selectedCampsite]);

  const hasClientInfo = !!user;
  const hasDates = !!arrivalDate && !!departureDate && !!numberOfNights;
  const hasSelectedSite = !!selectedCampsite?.campsiteId;

  useEffect(() => {
    if (!selectedCampsite?.campsiteId || !arrivalDate || !numberOfNights) {
      return;
    }

    const loadPrice = async () => {
      setPriceLoading(true);
      setPriceError("");

      try {
        const result = await api.post("/pricing-engine/calculate", {
          campsiteId: selectedCampsite.campsiteId,
          date: arrivalDate,
          nights: numberOfNights,
        });

        setPriceResult(result);
      } catch (err) {
        console.error(err);
        setPriceError("Le prix n’a pas pu être calculé automatiquement.");
      } finally {
        setPriceLoading(false);
      }
    };

    loadPrice();
  }, [selectedCampsite, arrivalDate, numberOfNights]);

  if (!selectedCampsite) {
    return (
      <div className="mx-auto max-w-5xl px-6 py-10">
        <button
          type="button"
          onClick={() => navigate("/reservation-test/results/campsites")}
          className="inline-flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900"
        >
          <ArrowLeft className="h-4 w-4" />
          Retour aux terrains
        </button>

        <div className="mt-6 rounded-2xl border bg-white p-6 text-gray-700">
          Aucun terrain sélectionné. Retourne à la liste des terrains pour en
          choisir un.
        </div>
      </div>
    );
  }

  const dailyPrices =
    priceResult?.breakdown ||
    priceResult?.dailyPrices ||
    priceResult?.details ||
    [];

  const totalPrice =
    priceResult?.total ||
    priceResult?.totalPrice ||
    priceResult?.amount ||
    null;

  const subtotal =
    dailyPrices.length > 0
      ? dailyPrices.reduce(
          (total, day) => total + Number(day.price || day.amount || 0),
          0
        )
      : totalPrice;

  return (
    <div className="mx-auto max-w-6xl px-6 py-10">
      <button
        type="button"
        onClick={() => navigate(-1)}
        className="inline-flex items-center gap-2 text-sm text-gray-600 hover:text-gray-900"
      >
        <ArrowLeft className="h-4 w-4" />
        Retour aux terrains disponibles
      </button>

      <div className="mt-5 rounded-3xl border bg-white p-6 shadow-sm">
        <div className="flex flex-col gap-4 md:flex-row md:items-start md:justify-between">
          <div>
            <h1 className="text-3xl font-bold text-gray-900">
              Confirmation de la réservation
            </h1>

            <p className="mt-2 text-gray-600">
              Vérifie les informations avant de passer au paiement.
            </p>
          </div>

          <CompatibilityBadge isCompatible={isLengthCompatible} />
        </div>

        <div className="mt-6 grid gap-5 lg:grid-cols-3">
          <div className="rounded-2xl border bg-gray-50 p-5 lg:col-span-2">
            <div className="mb-4 flex items-center gap-2">
              <CalendarDays className="h-5 w-5 text-orange-600" />
              <h2 className="text-xl font-bold text-gray-900">
                Résumé du séjour
              </h2>
            </div>

            <div className="grid gap-4 md:grid-cols-2">
              <InfoLine label="Camping" value={selectedCampsite.campgroundName} />
              <InfoLine
                label="Terrain"
                value={`Terrain ${selectedCampsite.siteCode}`}
              />
              <InfoLine label="Arrivée" value={formatDate(arrivalDate)} />
              <InfoLine label="Départ" value={formatDate(departureDate)} />
              <InfoLine
                label="Nombre de nuits"
                value={numberOfNights ? `${numberOfNights} nuit(s)` : "N/D"}
              />
              <InfoLine
                label="Distance"
                value={
                  selectedCampsite.distanceKm
                    ? `${selectedCampsite.distanceKm.toFixed(1)} km`
                    : "N/D"
                }
              />
            </div>
          </div>

          <div className="rounded-2xl border border-blue-200 bg-blue-50 p-5">
            <div className="mb-3 flex items-center gap-2">
              <ShieldCheck className="h-5 w-5 text-blue-700" />
              <h2 className="text-lg font-bold text-blue-950">
                Validation rapide
              </h2>
            </div>

            <div className="space-y-3">
              <SmallStatus ok={hasDates} label="Dates sélectionnées" />
              <SmallStatus ok={hasSelectedSite} label="Terrain sélectionné" />
              <SmallStatus ok={hasClientInfo} label="Client identifié" />
              <SmallStatus
                ok={isLengthCompatible}
                label={
                  isLengthCompatible
                    ? "Longueur compatible"
                    : "Longueur à vérifier"
                }
              />
              <SmallStatus
                ok={!priceError && !priceLoading}
                label={priceError ? "Prix à confirmer" : "Prix calculé"}
              />
            </div>
          </div>
        </div>

        <div className="mt-6 grid gap-5 lg:grid-cols-3">
          <div className="space-y-5 lg:col-span-2">
            <div className="rounded-2xl border bg-white p-5">
              <div className="mb-4 flex items-center gap-2">
                <UserRound className="h-5 w-5 text-blue-700" />
                <h2 className="text-xl font-bold text-gray-900">
                  Informations du client
                </h2>
              </div>

              <div className="grid gap-4 md:grid-cols-2">
                <InfoLine label="Nom complet" value={buildFullName(user)} />
                <InfoLine
                  label="Courriel"
                  value={getFirstValue(user?.email, user?.courriel)}
                />
                <InfoLine
                  label="Téléphone"
                  value={getFirstValue(user?.phone, user?.telephone, user?.tel)}
                />
                <InfoLine
                  label="Adresse"
                  value={getFirstValue(
                    user?.address,
                    user?.adresse,
                    user?.addressLine1,
                    user?.address_line_1
                  )}
                />
                <InfoLine label="Ville" value={getFirstValue(user?.city, user?.ville)} />
                <InfoLine
                  label="Province / État"
                  value={getFirstValue(
                    user?.provinceStateName,
                    user?.provinceState,
                    user?.province,
                    user?.state
                  )}
                />
                <InfoLine
                  label="Pays"
                  value={getFirstValue(user?.countryName, user?.country, user?.pays)}
                />
                <InfoLine
                  label="Code postal"
                  value={getFirstValue(
                    user?.postalCode,
                    user?.postal_code,
                    user?.codePostal
                  )}
                />
              </div>

              {!hasClientInfo && (
                <div className="mt-4 rounded-xl border border-yellow-200 bg-yellow-50 p-4 text-sm text-yellow-800">
                  Les informations du client ne sont pas disponibles. La
                  connexion ou le profil devra être validé avant le paiement.
                </div>
              )}
            </div>

            <div className="rounded-2xl border bg-white p-5">
              <div className="mb-4 flex items-center gap-2">
                <Info className="h-5 w-5 text-orange-600" />
                <h2 className="text-xl font-bold text-gray-900">
                  Équipement et critères demandés
                </h2>
              </div>

              <div className="grid gap-4 md:grid-cols-2">
                <InfoLine
                  label="Contexte équipement"
                  value={
                    searchCriteria?.useEquipmentContext
                      ? "Équipement actif utilisé"
                      : "Critères manuels"
                  }
                />
                <InfoLine
                  label="Longueur demandée"
                  value={formatFeet(requestedEquipmentLength)}
                />
                <InfoLine
                  label="Extension conducteur"
                  value={
                    searchCriteria?.hasDriverSideSlideOut
                      ? `${searchCriteria?.driverSideSlideOutCount || 0}`
                      : "Non"
                  }
                />
                <InfoLine
                  label="Extension passager"
                  value={
                    searchCriteria?.hasPassengerSideSlideOut
                      ? `${searchCriteria?.passengerSideSlideOutCount || 0}`
                      : "Non"
                  }
                />
              </div>

              <div className="mt-4 flex flex-wrap gap-2">
                {searchCriteria?.requiresWater && <ServiceBadge label="Eau demandée" />}
                {searchCriteria?.requiresElectricity && (
                  <ServiceBadge label="Électricité demandée" color="yellow" />
                )}
                {searchCriteria?.requiresSewer && (
                  <ServiceBadge label="Égout demandé" color="green" />
                )}
                {searchCriteria?.requires15_20Amp && (
                  <ServiceBadge label="15/20 amp demandé" color="orange" />
                )}
                {searchCriteria?.requires30Amp && (
                  <ServiceBadge label="30 amp demandé" color="orange" />
                )}
                {searchCriteria?.requires50Amp && (
                  <ServiceBadge label="50 amp demandé" color="red" />
                )}
              </div>
            </div>

            <div className="rounded-2xl border bg-white p-5">
              <div className="mb-4 flex items-center gap-2">
                <Tent className="h-5 w-5 text-green-700" />
                <h2 className="text-xl font-bold text-gray-900">
                  Détails du terrain
                </h2>
              </div>

              <div className="grid gap-4 md:grid-cols-2">
                <div className="rounded-xl bg-gray-50 p-4">
                  <div className="text-xs font-semibold uppercase tracking-wide text-gray-500">
                    Dimensions
                  </div>

                  <div className="mt-3 space-y-2 text-sm text-gray-700">
                    <InfoLine
                      label="Largeur"
                      value={formatFeet(selectedCampsite.widthFeet)}
                    />
                    <InfoLine
                      label="Longueur"
                      value={formatFeet(selectedCampsite.lengthFeet)}
                    />
                    <InfoLine
                      label="Longueur max équipement"
                      value={formatFeet(selectedCampsite.maxEquipmentLengthFeet)}
                    />
                    <InfoLine
                      label="Accès direct"
                      value={selectedCampsite.pullThrough ? "Oui" : "Non"}
                    />
                  </div>
                </div>

                <div className="rounded-xl bg-gray-50 p-4">
                  <div className="text-xs font-semibold uppercase tracking-wide text-gray-500">
                    Services et surface
                  </div>

                  <div className="mt-3 flex flex-wrap gap-2">
                    {selectedCampsite.hasWater && <ServiceBadge label="Eau" />}
                    {selectedCampsite.hasElectricity && (
                      <ServiceBadge label="Électricité" color="yellow" />
                    )}
                    {selectedCampsite.hasSewer && (
                      <ServiceBadge label="Égout" color="green" />
                    )}
                    {selectedCampsite.has15_20Amp && (
                      <ServiceBadge label="15/20 amp" color="orange" />
                    )}
                    {selectedCampsite.has30Amp && (
                      <ServiceBadge label="30 amp" color="orange" />
                    )}
                    {selectedCampsite.has50Amp && (
                      <ServiceBadge label="50 amp" color="red" />
                    )}

                    {!selectedCampsite.hasWater &&
                      !selectedCampsite.hasElectricity &&
                      !selectedCampsite.hasSewer &&
                      !selectedCampsite.has15_20Amp &&
                      !selectedCampsite.has30Amp &&
                      !selectedCampsite.has50Amp && (
                        <ServiceBadge label="Aucun service indiqué" color="gray" />
                      )}
                  </div>

                  <div className="mt-4 text-sm text-gray-700">
                    <span className="font-semibold">Surface :</span>{" "}
                    {formatSurfaceValues(selectedCampsite.surfaceValues)}
                  </div>
                </div>
              </div>

              <div className="mt-5 rounded-xl border border-orange-200 bg-orange-50 p-4 text-sm text-orange-900">
                <div className="flex gap-2">
                  <Info className="mt-0.5 h-4 w-4 flex-shrink-0" />
                  <p>
                    Avant de payer, assure-toi que le terrain, les dates, les
                    services et la longueur maximale conviennent à ton
                    équipement.
                  </p>
                </div>
              </div>
            </div>

            {selectedCampsite.photoUrls?.length > 0 && (
              <div className="rounded-2xl border bg-white p-5">
                <h2 className="mb-4 text-xl font-bold text-gray-900">
                  Photos du terrain
                </h2>

                <div className="grid gap-4 md:grid-cols-3">
                  {selectedCampsite.photoUrls.map((url) => (
                    <img
                      key={url}
                      src={url}
                      alt={`Terrain ${selectedCampsite.siteCode}`}
                      className="h-48 w-full rounded-2xl object-cover"
                    />
                  ))}
                </div>
              </div>
            )}
          </div>

          <div className="lg:sticky lg:top-6 lg:self-start">
            <div className="rounded-2xl border bg-white p-5 shadow-sm">
              <div className="mb-4 flex items-center gap-2">
                <CreditCard className="h-5 w-5 text-green-700" />
                <h2 className="text-xl font-bold text-gray-900">
                  Résumé des coûts
                </h2>
              </div>

              <div className="rounded-2xl bg-green-50 p-4">
                <div className="text-sm font-semibold text-green-800">
                  Total du séjour
                </div>
                <div className="mt-1 text-3xl font-bold text-green-900">
                  {formatMoney(totalPrice)}
                </div>
                <div className="mt-1 text-xs text-green-700">
                  {numberOfNights ? `${numberOfNights} nuit(s)` : "Durée à confirmer"}
                </div>
              </div>

              {priceLoading && (
                <div className="mt-4 rounded-xl bg-gray-50 p-4 text-sm text-gray-600">
                  Calcul du prix...
                </div>
              )}

              {priceError && (
                <div className="mt-4 rounded-xl border border-yellow-200 bg-yellow-50 p-4 text-sm text-yellow-800">
                  {priceError}
                </div>
              )}

              {!priceLoading && !priceError && dailyPrices.length > 0 && (
                <div className="mt-4 space-y-2">
                  {dailyPrices.map((day, index) => (
                    <div
                      key={`${day.date || index}-${index}`}
                      className="flex justify-between gap-3 text-sm text-gray-700"
                    >
                      <span>
                        {formatDate(day.date)}
                        {day.label ? (
                          <span className="block text-xs text-gray-500">
                            {day.label}
                          </span>
                        ) : null}
                      </span>

                      <span className="font-semibold">
                        {formatMoney(day.price || day.amount)}
                      </span>
                    </div>
                  ))}
                </div>
              )}

              <div className="mt-4 border-t pt-4">
                <div className="flex items-center justify-between text-sm">
                  <span className="text-gray-600">Sous-total</span>
                  <span className="font-semibold">
                    {formatMoney(subtotal)}
                  </span>
                </div>

                <div className="mt-2 flex items-center justify-between text-sm">
                  <span className="text-gray-600">Taxes / frais</span>
                  <span className="font-semibold">À confirmer</span>
                </div>

                <div className="mt-4 flex items-center justify-between border-t pt-4">
                  <span className="font-bold text-gray-900">Total</span>
                  <span className="text-2xl font-bold text-green-700">
                    {formatMoney(totalPrice)}
                  </span>
                </div>

                <p className="mt-2 text-xs text-gray-500">
                  Taxes et frais à intégrer selon la logique de paiement finale.
                </p>
              </div>

              <button
                type="button"
                className="mt-6 w-full rounded-xl bg-green-700 px-5 py-3 font-bold text-white hover:bg-green-800"
              >
                Continuer vers le paiement sécurisé
              </button>

              <button
                type="button"
                onClick={() => navigate(-1)}
                className="mt-3 w-full rounded-xl border px-5 py-3 font-semibold text-gray-700 hover:bg-gray-50"
              >
                Modifier le terrain
              </button>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}

function InfoLine({ label, value }) {
  return (
    <div>
      <div className="text-xs font-semibold uppercase tracking-wide text-gray-500">
        {label}
      </div>
      <div className="mt-1 font-semibold text-gray-900">{value || "N/D"}</div>
    </div>
  );
}

function CompatibilityBadge({ isCompatible }) {
  if (isCompatible) {
    return (
      <div className="inline-flex items-center gap-2 rounded-full bg-green-100 px-4 py-2 text-sm font-bold text-green-800">
        <CheckCircle2 className="h-4 w-4" />
        Compatible avec votre équipement
      </div>
    );
  }

  return (
    <div className="inline-flex items-center gap-2 rounded-full bg-red-100 px-4 py-2 text-sm font-bold text-red-800">
      <XCircle className="h-4 w-4" />À vérifier avant paiement
    </div>
  );
}

function SmallStatus({ ok, label }) {
  return (
    <div
      className={`flex items-center gap-2 rounded-xl px-3 py-2 text-sm font-semibold ${
        ok ? "bg-green-100 text-green-800" : "bg-red-100 text-red-800"
      }`}
    >
      {ok ? <CheckCircle2 className="h-4 w-4" /> : <XCircle className="h-4 w-4" />}
      {label}
    </div>
  );
}

function ServiceBadge({ label, color = "cyan" }) {
  const colorClassMap = {
    cyan: "bg-cyan-100 text-cyan-700",
    yellow: "bg-yellow-100 text-yellow-700",
    green: "bg-green-100 text-green-700",
    orange: "bg-orange-100 text-orange-700",
    red: "bg-red-100 text-red-700",
    gray: "bg-gray-100 text-gray-700",
  };

  return (
    <span
      className={`rounded-full px-3 py-1 text-xs font-bold ${
        colorClassMap[color] || colorClassMap.cyan
      }`}
    >
      {label}
    </span>
  );
}