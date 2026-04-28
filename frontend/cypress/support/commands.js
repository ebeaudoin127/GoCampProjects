// cypress/support/commands.js

// IMPORTANT : NOUS NE METTONS PAS LE TOKEN ICI
// (car React chargerait avant) → onBeforeLoad s'en charge dans les tests

// Mock de l’utilisateur /api/auth/me
Cypress.Commands.add("loginMock", () => {
  cy.intercept("GET", "http://localhost:8080/api/auth/me", {
    statusCode: 200,
    body: {
      firstname: "John",
      lastname: "Doe",
      email: "john@example.com",
      role: "User",
    },
  }).as("getUser");
});
