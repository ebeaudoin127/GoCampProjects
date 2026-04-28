// cypress/e2e/profile.cy.js

describe("Mise à jour du profil", () => {
  beforeEach(() => {
    cy.intercept("PUT", "http://localhost:8080/api/auth/update-profile", {
      statusCode: 200,
      body: {
        firstname: "Jane",
        lastname: "Doe",
        email: "john@example.com",
        role: "User",
      }
    }).as("updateProfile");
  });

  it("met à jour le profil avec succès", () => {
    cy.visit("/account");
    cy.wait("@getUser");

    cy.get("input").first().clear().type("Jane");
    cy.contains("Sauvegarder").click();

    cy.wait("@updateProfile");
    cy.contains("Profil mis à jour avec succès !").should("exist");
  });
});
