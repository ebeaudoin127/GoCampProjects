// ============================================================
// Fichier : src/components/IconRenderer.jsx
// Dernière modification : 2026-04-17
// Auteur : ChatGPT
//
// Résumé :
// - Moteur central d'affichage des icônes
// - Utilise d'abord les icônes custom
// - Sinon utilise lucide-react
// - Sinon affiche une icône par défaut
// ============================================================

import React from "react";
import * as LucideIcons from "lucide-react";

import WashingMachineIcon from "./icons/WashingMachineIcon";
import ShowerIcon from "./icons/ShowerIcon";
import RvIcon from "./icons/RvIcon";
import DumpStationIcon from "./icons/DumpStationIcon";
import ReadyToCampIcon from "./icons/ReadyToCampIcon";
import YurtIcon from "./icons/YurtIcon";
import CabinIcon from "./icons/CabinIcon";

const customIcons = {
  "washing-machine": WashingMachineIcon,
  shower: ShowerIcon,
  rv: RvIcon,
  "dump-station": DumpStationIcon,
  "ready-to-camp": ReadyToCampIcon,
  yurt: YurtIcon,
  cabin: CabinIcon,
};

const lucideMap = {
  wifi: "Wifi",
  droplet: "Droplets",
  water: "Droplets",
  electricity: "Zap",
  zap: "Zap",
  flame: "Flame",
  recycle: "Recycle",
  car: "Car",
  dog: "Dog",
  shield: "Shield",
  bike: "Bike",
  mountain: "Mountain",
  fish: "Fish",
  flag: "Flag",
  waves: "Waves",
  umbrella: "Umbrella",
  tree: "TreePine",
  home: "House",
  tent: "Tent",
  restaurant: "Utensils",
  utensils: "Utensils",
  snowflake: "Snowflake",
  truck: "Truck",
  toilet: "Toilet",
  accessibility: "Accessibility",
  accessibility_icon: "Accessibility",
  "accessibility-icon": "Accessibility",
};

export default function IconRenderer({
  name,
  className = "w-4 h-4",
  strokeWidth = 2,
}) {
  if (!name) {
    const Fallback = LucideIcons.HelpCircle;
    return <Fallback className={className} strokeWidth={strokeWidth} />;
  }

  const normalized = String(name).trim().toLowerCase();

  const CustomIcon = customIcons[normalized];
  if (CustomIcon) {
    return <CustomIcon className={className} />;
  }

  const lucideName = lucideMap[normalized] || normalized;
  const LucideIcon = LucideIcons[lucideName] || LucideIcons.HelpCircle;

  return <LucideIcon className={className} strokeWidth={strokeWidth} />;
}