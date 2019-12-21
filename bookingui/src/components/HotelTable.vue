<template>
  <!-- :expanded.sync="expanded" -->
  <v-data-table
    :headers="headers"
    :items="items"
    :single-expand="false"
    item-key="name"
    show-expand
    class="elevation-1"
    :search="search"
    :loading="isLoading" 
    loading-text="Loading... Please wait"
  >
    <template v-slot:top>
      <v-toolbar flat>
        <v-toolbar-title>Hotels</v-toolbar-title>
        <v-spacer></v-spacer>
      </v-toolbar>
        <v-text-field v-model="search" label="Search" class="mx-4"></v-text-field>
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
      itemsPerPageArray: [4, 8, 12],
      search: "",
      isLoading:true,
      filter: {},
      sortDesc: false,
      page: 1,
      itemsPerPage: 4,
      sortBy: "name",
      keys: ["name", "star", "locality", "address"],
      headers: [
        {
          text: "name",
          align: "left",
          value: "name"
        },
        { text: "Star", value: "star" },
        { text: "Locality", value: "locality" },
        { text: "Address", value: "address" },
        { text: '', value: 'data-table-expand' },
      ],
      items: []
    };
  },
  computed: {
    numberOfPages() {
      return Math.ceil(this.items.length / this.itemsPerPage);
    },
    filteredKeys() {
      return this.keys.filter(key => key !== `Name`);
    }
  },
  methods: {
    getHotelList() {
      var vm = this;
      vm.isLoading=true;

      vm.axios
        .get("Hotel/all")
        .then(response => {
          // if(response.)
          vm.items = response.data;
          console.log("i success");
        })
        .catch(error => {
          console.log(error);
          console.warn("Not good man :(");
        })
        .finally(function () {
          // always executed
          vm.isLoading=false;
        });
    },
    nextPage() {
      if (this.page + 1 <= this.numberOfPages) this.page += 1;
    },
    formerPage() {
      if (this.page - 1 >= 1) this.page -= 1;
    },
    updateItemsPerPage(number) {
      this.itemsPerPage = number;
    }
  },
  mounted: function() {
    this.getHotelList();
  }
};
</script>