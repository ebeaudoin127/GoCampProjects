// cypress/e2e/register.cy.js

describe("Inscription", () => {

  beforeEach(() => {
    cy.clearLocalStorage();
  });

  it("[noLogin] affiche la page inscription", () => {
    cy.visit("/auth");
    cy.contains("Créer un compte").should("exist");
  });

  it("[noLogin] erreur champs vides", () => {
    cy.visit("/auth");
    cy.contains("Créer un compte").click();

    cy.contains("Veuillez remplir tous les champs").should("exist");
  });

  it("[noLogin] email déjà utilisé", () => {
    cy.intercept("POST", "http://localhost:8080/api/auth/register", {
      statusCode: 400,
      body: { message: "Cet email est déjà utilisé." }
    }).as("emailUsed");

    cy.visit("/auth");

    cy.get('[name="firstname"]').type("John");
    cy.get('[name="lastname"]').type("Doe");
    cy.get('[name="email"]').type("john@example.com");
    cy.get('[name="password"]').type("1234");

    cy.contains("Créer un compte").click();

    cy.wait("@emailUsed");
    cy.contains("Cet email est déjà utilisé.").should("exist");
  });


  it("[noLogin] inscription OK & redirection vers /account", () => {
    cy.intercept("POST", "http://localhost:8080/api/auth/register", {
      statusCode: 200,
      body: { token: "FAKE_REGISTER_TOKEN" }
    }).as("registerSuccess");

    cy.visit("/auth");

    cy.get('[name="firstname"]').type("John");
    cy.get('[name="lastname"]').type("Doe");
    cy.get('[name="email"]').type("john@example.com");
    cy.get('[name="password"]').type("1234");

    cy.contains("Créer un compte").click();

    cy.wait("@registerSuccess");

    cy.window().then((win) => {
      expect(win.localStorage.getItem("token")).to.equal("FAKE_REGISTER_TOKEN");
    });

    cy.url().should("include", "/account");
  });

});
