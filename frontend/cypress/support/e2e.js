// cypress/support/e2e.js
import "./commands";

// Détection des tests [noLogin]
function isNoLoginTest() {
  return Cypress.mocha
    .getRunner()
    .test.fullTitle()
    .includes("[noLogin]");
}

// loginMock AVANT chaques tests SAUF [noLogin]
beforeEach(() => {
  if (!isNoLoginTest()) {
    cy.loginMock();
  }
});
