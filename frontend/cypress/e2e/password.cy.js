// cypress/e2e/password.cy.js

describe("Changement de mot de passe", () => {
  beforeEach(() => {
    cy.visit("/account");
    cy.wait("@getUser");
    cy.contains("Sécurité").click();
  });

  it("affiche erreur si ancien mot de passe incorrect", () => {
    cy.intercept("PUT", "http://localhost:8080/api/auth/change-password", {
      statusCode: 400,
      body: { message: "Ancien mot de passe incorrect." }
    }).as("badPw");

    cy.get('input[type="password"]').eq(0).type("wrong");
    cy.get('input[type="password"]').eq(1).type("newpass");
    cy.get('input[type="password"]').eq(2).type("newpass");

    cy.contains("Changer le mot de passe").click();

    cy.wait("@badPw");
    cy.contains("Ancien mot de passe incorrect.").should("exist");
  });
});
