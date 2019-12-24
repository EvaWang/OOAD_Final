<template>
  <v-container class="fluid">
    <v-row justify="center">
      <v-expansion-panels>
        <v-expansion-panel>
          <v-expansion-panel-header>Search</v-expansion-panel-header>
          <v-expansion-panel-content>
            <SearchPanel></SearchPanel>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </v-row>
            <HotelTable></HotelTable>
  </v-container>
</template>

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
    //
    items: [
      {
        title: "測試測試"
      }
    ]
  }),
  methods: {}
};
</script>
