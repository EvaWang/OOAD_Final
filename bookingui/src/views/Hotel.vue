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
       <!-- :stars="[1, 2]" 
    :locality="locality"
    :roomType="roomType"
    :startDate="startDate"
    :endDate="endDate" -->
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
    locality:"",
    stars:[],
    startDate:"",
    roomType:1,
    endDate:"",
    items: [
      {
        title: "測試測試"
      }
    ]
  }),
  methods: {
    // searchClick(val){
    //   console.log(val)
    // }
  }
};
</script>
