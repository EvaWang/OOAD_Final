<template>
    <!-- :search="search" -->

  <v-data-table
    :headers="headers"
    :items="items"
    :single-expand="false"
    :options.sync="options"
    item-key="name"
    show-expand
    class="elevation-1"
    :loading="isLoading"
    loading-text="Loading... Please wait"
    :server-items-length="pageLength"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Hotels</v-toolbar-title>
        <v-spacer></v-spacer>
      </v-toolbar>
      <!-- <v-text-field v-model="search" label="Search" class="mx-4"></v-text-field> -->
    </template>
    <template v-slot:expanded-item="{ headers }">
      <td :colspan="headers.length">Peek-a-boo!</td>
    </template>
  </v-data-table>
</template>

<script>
export default {
  data() {
    return {
      search: "",
      isLoading: true,
      headers: [
        {
          text: "name",
          align: "left",
          value: "name"
        },
        { text: "Star", value: "star" },
        { text: "Locality", value: "locality" },
        { text: "Address", value: "address" },
        { text: "", value: "data-table-expand" }
      ],
      items: [],
      pageLength: 1,
      options: {}
    };
  },
  computed: {},
  watch: {
    options: {
      handler() {
        // this.getHotelList();
      },
      deep: true
    },
    search:{
      handler() {
        this.getHotelList();
      }
    },
  },
  methods: {
    getHotelList() {
      var vm = this;
      vm.isLoading = true;
      vm.axios
        .get("Hotel/all", {
          params: {
            page: vm.options.page,
            size: vm.options.itemsPerPage,
            sortKey:vm.options.sortBy[0],
            sortDesc:vm.options.sortDesc[0],
            search: vm.search
          }
        })
        .then(response => {
          // if(response.)
          vm.items = response.data.content;
          vm.pageLength = response.data.totalPages;
          console.log("i success");
        })
        .catch(error => {
          console.log(error);
          console.warn("Not good man :(");
        })
        .finally(function() {
          // always executed
          vm.isLoading = false;
        });
    }
  },
  mounted: function() {
    this.getHotelList();
  }
};
</script>