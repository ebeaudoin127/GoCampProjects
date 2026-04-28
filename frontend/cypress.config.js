const { defineConfig } = require("cypress");

module.exports = defineConfig({
  e2e: {
    baseUrl: "http://localhost:5173",
    env: {
      api: "http://localhost:8080"
    },
    defaultCommandTimeout: 8000,
    chromeWebSecurity: false,
    video: false,
    setupNodeEvents(on, config) {
      // (facultatif)
    },
  },
});
