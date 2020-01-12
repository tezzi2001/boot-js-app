module.exports = {
  transpileDependencies: ["vuetify"],
  devServer: {
    proxy: "https://spring-boot-rest-api-app.herokuapp.com/",
    port: 8080
  }
};
