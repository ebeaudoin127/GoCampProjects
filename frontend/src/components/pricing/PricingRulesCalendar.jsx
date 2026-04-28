
// ============================================================
// Fichier : frontend/src/components/pricing/PricingRulesCalendar.jsx
// Dernière modification : 2026-04-24
//
// Résumé :
// - Vue calendrier type Excel pour les règles tarifaires
// - Lignes = regroupements
// - Colonnes = dates
// - En-têtes mois + dates
// - Colonne regroupement sticky à gauche
// - Prix + minimum de nuits dans les cellules
// - Défilement horizontal limité au calendrier
// ============================================================

import React, { useMemo } from "react";

const JS_DAY_TO_PRICING_DAY = {
  0: "SUNDAY",
  1: "MONDAY",
  2: "TUESDAY",
  3: "WEDNESDAY",
  4: "THURSDAY",
  5: "FRIDAY",
  6: "SATURDAY",
};

function formatCurrency(value) {
  if (value === null || value === undefined || value === "") return "";
  return `${Number(value).toFixed(0)}$`;
}

function toDateKey(date) {
  return date.toISOString().slice(0, 10);
}

function getDaysBetween(startDate, endDate) {
  const days = [];
  const current = new Date(`${startDate}T00:00:00`);
  const end = new Date(`${endDate}T00:00:00`);

  while (current <= end) {
    days.push(new Date(current));
    current.setDate(current.getDate() + 1);
  }

  return days;
}

function getDateRangeFromRules(rules) {
  if (!Array.isArray(rules) || rules.length === 0) {
    const today = new Date();
    const start = new Date(today.getFullYear(), today.getMonth(), 1);
    const end = new Date(today.getFullYear(), today.getMonth() + 1, 0);

    return {
      startDate: toDateKey(start),
      endDate: toDateKey(end),
    };
  }

  return {
    startDate: rules.map((r) => r.startDate).filter(Boolean).sort()[0],
    endDate: rules.map((r) => r.endDate).filter(Boolean).sort().at(-1),
  };
}

function buildMonthSpans(days) {
  const spans = [];

  days.forEach((date) => {
    const key = `${date.getFullYear()}-${date.getMonth()}`;
    const label = date.toLocaleDateString("fr-CA", { month: "long" });
    const last = spans[spans.length - 1];

    if (last && last.key === key) {
      last.count += 1;
    } else {
      spans.push({ key, label, count: 1 });
    }
  });

  return spans;
}

function ruleAppliesToDate(rule, date) {
  const dateKey = toDateKey(date);

  if (dateKey < rule.startDate || dateKey > rule.endDate) {
    return false;
  }

  if (!Array.isArray(rule.daysOfWeek) || rule.daysOfWeek.length === 0) {
    return true;
  }

  return rule.daysOfWeek.includes(JS_DAY_TO_PRICING_DAY[date.getDay()]);
}

function resolveRuleForGroupAndDate(rules, group, date) {
  const matches = rules.filter((rule) => {
    return (
      rule.targetType === "GROUP" &&
      String(rule.pricingOptionId) === String(group.pricingOptionId) &&
      ruleAppliesToDate(rule, date)
    );
  });

  if (matches.length === 0) return null;

  return matches.sort((a, b) => {
    if (a.startDate > b.startDate) return -1;
    if (a.startDate < b.startDate) return 1;
    return 0;
  })[0];
}

function getPriceLabel(rule) {
  if (!rule) return "";

  if (rule.pricingType === "FIXED") {
    return formatCurrency(rule.fixedPrice);
  }

  if (rule.dynamicBasePrice !== null && rule.dynamicBasePrice !== undefined) {
    return formatCurrency(rule.dynamicBasePrice);
  }

  return "";
}

function getMinNightsLabel(rule) {
  if (!rule?.minimumNights) return "";
  return `${rule.minimumNights}N`;
}

function getCellClass(rule) {
  if (!rule) {
    return "bg-white text-slate-300";
  }

  if (rule.minimumNights && rule.minimumNights >= 2) {
    return "bg-orange-200 text-slate-900";
  }

  if (rule.pricingType === "DYNAMIC") {
    return "bg-yellow-200 text-slate-900";
  }

  return "bg-emerald-500 text-white";
}

function getTooltip(rule, group) {
  if (!rule) return "";

  const price =
    rule.pricingType === "FIXED"
      ? `Prix fixe : ${formatCurrency(rule.fixedPrice)}`
      : `Dynamique : min ${formatCurrency(rule.dynamicMinPrice)} / réf ${formatCurrency(
          rule.dynamicBasePrice
        )} / max ${formatCurrency(rule.dynamicMaxPrice)}`;

  const minNights = rule.minimumNights
    ? `Minimum : ${rule.minimumNights} nuit(s)`
    : "Aucun minimum de nuits";

  return [
    rule.label || group.pricingOptionName || "Règle tarifaire",
    `${rule.startDate} au ${rule.endDate}`,
    price,
    minNights,
  ].join(" | ");
}

export default function PricingRulesCalendar({
  rules = [],
  groups = [],
  selectedGroupFilter = "ALL",
}) {
  const visibleGroups = useMemo(() => {
    if (selectedGroupFilter !== "ALL") {
      return groups.filter(
        (group) => String(group.pricingOptionId) === String(selectedGroupFilter)
      );
    }

    return groups;
  }, [groups, selectedGroupFilter]);

  const days = useMemo(() => {
    const range = getDateRangeFromRules(rules);
    return getDaysBetween(range.startDate, range.endDate);
  }, [rules]);

  const monthSpans = useMemo(() => buildMonthSpans(days), [days]);

  const gridTemplateColumns = `220px repeat(${days.length}, 64px)`;

  return (
    <div className="rounded-3xl border bg-white shadow-sm overflow-hidden">
      <div className="border-b bg-slate-50 px-6 py-5">
        <h2 className="text-xl font-semibold text-slate-900">
          Vue calendrier par regroupement
        </h2>

        <p className="text-sm text-slate-600 mt-1">
          Format Excel : regroupements à gauche, dates en haut, prix dans les cellules.
        </p>

        <div className="mt-4 flex flex-wrap gap-4 text-xs text-slate-700">
          <span className="inline-flex items-center gap-2">
            <span className="h-3 w-3 rounded bg-emerald-500" />
            Prix fixe
          </span>

          <span className="inline-flex items-center gap-2">
            <span className="h-3 w-3 rounded bg-yellow-200 border" />
            Dynamique
          </span>

          <span className="inline-flex items-center gap-2">
            <span className="h-3 w-3 rounded bg-orange-200 border" />
            Minimum de nuits
          </span>
        </div>
      </div>

      <div className="max-h-[520px] overflow-auto">
        <div className="min-w-max">
          <div
            className="grid sticky top-0 z-30 bg-white border-b"
            style={{ gridTemplateColumns }}
          >
            <div className="sticky left-0 z-40 border-r bg-white px-3 py-2 text-xs font-bold text-slate-900">
              Regroupement
            </div>

            {monthSpans.map((span) => (
              <div
                key={span.key}
                className="border-r bg-slate-100 px-2 py-2 text-center text-xs font-bold text-slate-900 capitalize"
                style={{ gridColumn: `span ${span.count}` }}
              >
                {span.label}
              </div>
            ))}
          </div>

          <div
            className="grid sticky top-[33px] z-30 bg-white border-b"
            style={{ gridTemplateColumns }}
          >
            <div className="sticky left-0 z-40 border-r bg-white px-3 py-2 text-xs font-semibold text-slate-700">
              Dates
            </div>

            {days.map((date) => {
              const isWeekend = date.getDay() === 0 || date.getDay() === 6;

              return (
                <div
                  key={toDateKey(date)}
                  className={`border-r px-1 py-2 text-center text-xs ${
                    isWeekend ? "bg-slate-200" : "bg-white"
                  }`}
                >
                  <div className="font-bold text-slate-900">{date.getDate()}</div>
                  <div className="text-[10px] capitalize text-slate-500">
                    {date.toLocaleDateString("fr-CA", { weekday: "short" })}
                  </div>
                </div>
              );
            })}
          </div>

          {visibleGroups.length === 0 ? (
            <div className="p-6 text-sm text-slate-600">
              Aucun regroupement à afficher.
            </div>
          ) : (
            visibleGroups.map((group) => (
              <div
                key={group.key}
                className="grid border-b"
                style={{ gridTemplateColumns }}
              >
                <div className="sticky left-0 z-20 border-r bg-white px-3 py-3 text-xs font-bold text-slate-900">
                  <div className="truncate">{group.pricingOptionName}</div>
                  <div className="mt-1 text-[10px] font-normal text-slate-500">
                    {group.sites?.length || 0} site(s)
                  </div>
                </div>

                {days.map((date) => {
                  const rule = resolveRuleForGroupAndDate(rules, group, date);
                  const price = getPriceLabel(rule);
                  const minNights = getMinNightsLabel(rule);

                  return (
                    <div
                      key={`${group.key}-${toDateKey(date)}`}
                      title={getTooltip(rule, group)}
                      className={`min-h-[54px] border-r px-1 py-2 text-center text-[11px] font-semibold leading-tight ${getCellClass(
                        rule
                      )}`}
                    >
                      {rule ? (
                        <>
                          <div>{price}</div>
                          {minNights && (
                            <div className="mt-1 text-[10px] font-bold">
                              {minNights}
                            </div>
                          )}
                        </>
                      ) : (
                        <span>-</span>
                      )}
                    </div>
                  );
                })}
              </div>
            ))
          )}
        </div>
      </div>
    </div>
  );
}
