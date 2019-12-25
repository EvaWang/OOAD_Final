<template>
  <v-container class="fluid">
    <v-row class="hotel-container">
      <v-col cols="12" md="4">
        <SearchPanel></SearchPanel>
      </v-col>
      <v-col cols="12" md="8">
        <HotelTable></HotelTable>
      </v-col>
    </v-row>
  </v-container>
</template>
<style scoped>
  .hotel-container{
    margin-top: 48px;
  }
</style>
<script>
import HotelTable from "@/components/HotelTable";
import SearchPanel from "@/components/SearchPanel.vue";

export default {
  name: "Hotel",
  components: {
    HotelTable,
    SearchPanel
  },
  data: () => ({
  }),
  methods: {
  }
};
</script>
