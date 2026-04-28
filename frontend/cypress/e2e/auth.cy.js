// cypress/e2e/auth.cy.js

describe("Authentification - non connecté", () => {
  it("[noLogin] redirige /account vers /auth", () => {
    cy.clearLocalStorage();

    cy.intercept("GET", "http://localhost:8080/api/auth/me", {
      statusCode: 401,
      body: {}
    }).as("noUser");

    cy.visit("/account");
    cy.wait("@noUser");

    cy.url().should("include", "/auth");
  });
});
