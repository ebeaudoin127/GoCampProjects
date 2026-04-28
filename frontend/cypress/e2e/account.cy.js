// cypress/e2e/account.cy.js

describe("Page /account", () => {

  beforeEach(() => {
    // Mock GET /me (IMPORTANT)
    cy.intercept("GET", "http://localhost:8080/api/auth/me", {
      statusCode: 200,
      body: {
        firstname: "John",
        lastname: "Doe",
        email: "john@example.com",
        role: "User",
        phone: "1234567890",
        address: "123 Rue",
        city: "Montreal",
        province: "QC",
        country: "Canada",
        postalCode: "H1H1H1",
      },
    }).as("getUser");

    // Mock PUT update-profile (empêche le 403)
    cy.intercept("PUT", "http://localhost:8080/api/auth/update-profile", {
      statusCode: 200,
      body: {
        firstname: "John",
        lastname: "Doe",
        email: "john@example.com",
        role: "User",
      },
    }).as("updateProfileMock");
  });

  it("affiche les infos du profil quand connecté", () => {

    // Injecte le token AVANT React
    cy.visit("/account", {
      onBeforeLoad(win) {
        win.localStorage.setItem("token", "TEST_TOKEN");
      },
    });

    // Attendre que le mock /me réponde
    cy.wait("@getUser");

    // Attendre l'animation Framer Motion
    cy.contains("Informations du profil", { timeout: 6000 })
      .should("exist")
      .and("be.visible");

    // Vérifier le champ prénom
    cy.get("input")
      .first()
      .should("have.value", "John");
  });

});
