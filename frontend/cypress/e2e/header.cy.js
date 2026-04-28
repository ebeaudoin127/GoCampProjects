// cypress/e2e/header.cy.js

describe("Header - connecté", () => {
  it("affiche Mon compte quand connecté", () => {
    cy.visit("/");
    cy.wait("@getUser");

    cy.contains("Mon compte").should("exist");
  });

  it("cache Se connecter quand connecté", () => {
    cy.visit("/");
    cy.contains("Se connecter").should("not.exist");
  });
});


describe("Header - non connecté", () => {
  it("[noLogin] n'affiche pas Mon compte", () => {
    cy.clearLocalStorage();
    cy.visit("/");
    cy.contains("Mon compte").should("not.exist");
  });

  it("[noLogin] affiche Se connecter", () => {
    cy.clearLocalStorage();
    cy.visit("/");
    cy.contains("Se connecter").should("exist");
  });
});
