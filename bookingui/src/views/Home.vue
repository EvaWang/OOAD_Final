<template>
  <v-container>
    <v-row justify="center">
      <v-col cols="auto">
        <h1 style="color:transparent">Welcome</h1>
        <h1>Welcome</h1>
      </v-col>
    </v-row>
    <v-row justify="center" class="pt-4">
      <v-col cols="11" md="8">
        <SearchPanel @searchClick="$router.push({ path: 'hotel'}).catch(err => {})"></SearchPanel>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
// @ is an alias to /src
import SearchPanel from "@/components/SearchPanel.vue";

export default {
  name: "home",
  components: {
    SearchPanel
  }
};
</script>
