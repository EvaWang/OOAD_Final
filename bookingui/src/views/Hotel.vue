<template>
    <HotelTable></HotelTable>
</template>

<script>
import HotelTable from "@/components/HotelTable";

export default {
  name: "Hotel",

  components: {
    HotelTable
  },

  data: () => ({
    //
    items: [
      {
        title: "測試測試"
      }
    ]
  }),
  methods: {
  }
};
</script>
