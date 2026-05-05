
// ============================================================
// Fichier : frontend/src/App.jsx
// Dernière modification : 2026-05-05
//
// Résumé des modifications :
// - Ajout promotions marketing
// - Ajout promotions dynamiques pricing
// - Ajout calculateur de prix
// - Correction du conflit de routes promotions
// ============================================================

import React from "react";
import { BrowserRouter, Routes, Route, Navigate } from "react-router-dom";
import { AuthProvider, useAuth } from "./context/AuthContext";

import Header from "./components/Header";
import Footer from "./components/Footer";
import ProtectedRoute from "./components/ProtectedRoute";

import HomePage from "./pages/HomePage";
import AuthPage from "./pages/AuthPage";
import AccountPage from "./pages/AccountPage";

import SiteManagerHome from "./pages/admin/SiteManagerHome";
import CampingManagerHome from "./pages/admin/CampingManagerHome";
import SiteUsersPage from "./pages/admin/SiteUsersPage";
import SiteCampgroundsPage from "./pages/admin/SiteCampgroundsPage";

import CreateCampgroundPage from "./pages/admin/CreateCampgroundPage";
import EditCampgroundPage from "./pages/admin/EditCampgroundPage";
import CampgroundPromotionsPage from "./pages/admin/CampgroundPromotionsPage";

import CampsitesPage from "./pages/admin/CampsitesPage";
import CreateCampsitePage from "./pages/admin/CreateCampsitePage";
import EditCampsitePage from "./pages/admin/EditCampsitePage";

import CampgroundMapPage from "./pages/admin/CampgroundMapPage";
import CampgroundPricingWizardPage from "./pages/admin/CampgroundPricingWizardPage";
import CampsitePricingRulesPage from "./pages/admin/CampsitePricingRulesPage";

import PricingPromotionsPage from "./pages/admin/PricingPromotionsPage";
import PriceCalculatorPage from "./pages/admin/PriceCalculatorPage";

function ScrollToTop() {
  React.useEffect(() => {
    window.scrollTo(0, 0);
  });

  return null;
}

function AuthRoute() {
  const { loading, user, getDefaultRouteForRole } = useAuth();

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center">
        Chargement...
      </div>
    );
  }

  if (user) {
    return <Navigate to={getDefaultRouteForRole(user)} replace />;
  }

  return <AuthPage />;
}

function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<HomePage />} />
      <Route path="/auth" element={<AuthRoute />} />

      <Route
        path="/account"
        element={
          <ProtectedRoute>
            <AccountPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <SiteManagerHome />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/users"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <SiteUsersPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <SiteCampgroundsPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/new"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <CreateCampgroundPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:id/edit"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <EditCampgroundPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:campgroundId/promotions"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <CampgroundPromotionsPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:campgroundId/pricing-promotions"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <PricingPromotionsPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:campgroundId/price-calculator"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <PriceCalculatorPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:campgroundId/sites"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <CampsitesPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:campgroundId/sites/new"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <CreateCampsitePage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:campgroundId/sites/:siteId/edit"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <EditCampsitePage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:campgroundId/sites/:siteId/pricing-rules"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <CampsitePricingRulesPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:campgroundId/map"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <CampgroundMapPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/site-manager/campgrounds/:campgroundId/pricing"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN"]}>
            <CampgroundPricingWizardPage />
          </ProtectedRoute>
        }
      />

      <Route
        path="/camping-manager"
        element={
          <ProtectedRoute allowedRoles={["SUPER_ADMIN", "CAMPING_ADMIN", "GESTIONNAIRE"]}>
            <CampingManagerHome />
          </ProtectedRoute>
        }
      />

      <Route path="*" element={<Navigate to="/" replace />} />
    </Routes>
  );
}

export default function App() {
  return (
    <AuthProvider>
      <BrowserRouter>
        <ScrollToTop />
        <Header />
        <AppRoutes />
        <Footer />
      </BrowserRouter>
    </AuthProvider>
  );
}