<template>
  <transition-group
    enter-active-class="animated fadeIn"
    leave-active-class="animated fadeOut"
    class="news-wrapper d-flex flex-wrap full-height justify-sm-center"
    tag="div"
  >
    <app-news-card
      v-for="(newsCard, index) in news"
      :key="newsCard.id"
      :newsCardData="newsCard"
      :index="index"
    ></app-news-card>
  </transition-group>
</template>

<script>
import NewsCard from "./News/NewsCard";

import { mapGetters } from "vuex";

export default {
  data() {
    return {};
  },
  computed: {
    ...mapGetters(["news", "isAdmin"])
  },
  components: {
    "app-news-card": NewsCard
  }
};
</script>

<style lang="scss" scoped>
.animated {
  animation-duration: 0.5s;
  transition-duration: 0.5s;
}
</style>
