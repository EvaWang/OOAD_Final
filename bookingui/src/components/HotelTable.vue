<template>
  <v-data-table
    :headers="headers"
    :items="items"
    group-by="star"
    :single-expand="false"
    :options.sync="options"
    item-key="name"
    show-expand
    class="elevation-1"
    :loading="isLoading"
    loading-text="Loading... Please wait"
    :server-items-length="pageLength"
  >
    <template v-slot:top> </template>
    <template v-slot:item.action="{ item }">
      <v-icon
        small
        class="mr-2"
        @click="editItem(item)"
      >
        edit
      </v-icon>
      <v-icon
        small
        @click="deleteItem(item)"
      >
        delete
      </v-icon>
    </template>
    <template v-slot:expanded-item="{ headers, item }">
      <td :colspan="headers.length">
        <HotelDetail
          :title="'Single Room'"
          :imgPath="require('../assets/single.jpg')"
          :Price="item.singleRoomPrice"
          :Quantity="item.singleRoom - item.bookedSingleRoom"
          :HotelId="item.jsonFileId"
          :RoomType="1"
          :StartDate="search.startDate"
          :EndDate="search.endDate"
          :showCheckout="true"
        ></HotelDetail>
        <HotelDetail
          :title="'Double Room'"
          :imgPath="require('../assets/double.jpg')"
          :Price="item.doubleRoomPrice"
          :Quantity="item.doubleRoom - item.bookedDoubleRoom"
          :HotelId="item.jsonFileId"
          :RoomType="2"
          :StartDate="search.startDate"
          :EndDate="search.endDate"
          :showCheckout="true"
        ></HotelDetail>
        <HotelDetail
          :title="'Double Room'"
          :imgPath="require('../assets/quad.jpg')"
          :Price="item.quadRoomPrice"
          :Quantity="item.quadRoom - item.bookedQuadRoom"
          :HotelId="item.jsonFileId"
          :RoomType="4"
          :StartDate="search.startDate"
          :EndDate="search.endDate"
          :showCheckout="true"
        ></HotelDetail>
      </td>
    </template>
  </v-data-table>
</template>

<script>
import { mapState } from "vuex";
import HotelDetail from "@/components/HotelDetail";

export default {
  name: "HotelTable",
  components: {
    HotelDetail
  },
  data() {
    return {
      search: {
        page: 0,
        size: 10,
        sortKey: "",
        sortDesc: "",
        stars: null,
        locality: null,
        roomType: null,
        startDate: null,
        endDate: null
      },
      isLoading: true,
      headers: [
        {
          text: "Name",
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
  computed: mapState(["searchCondition"]),
  watch: {
    options: {
      handler() {
        // console.log("options fires");
        this.getHotelList();
      },
      deep: true
    },
    searchCondition: {
      handler() {
        // console.log("searchCondition fires");
        this.getHotelList();
      },
      deep: true
    }
  },
  methods: {
    starArray2list() {
      var vm = this;
      var starList = [];
      if (vm.searchCondition.stars && vm.searchCondition.stars.length > 0) {
        for (var i = 0; i < 5; i++) {
          if (vm.searchCondition.stars[i]) {
            starList.push(i + 1);
          }
        }
        return starList.join(",");
      } else {
        return null;
      }
    },
    getHotelList() {
      var vm = this;
      vm.search.page = vm.options.page;
      vm.search.size = vm.options.itemsPerPage;
      vm.search.sortKey = vm.options.sortBy[0];
      vm.search.sortDesc = vm.options.sortDesc[0];
      vm.search.stars = vm.starArray2list();
      vm.search.locality = vm.searchCondition.locality;
      vm.search.roomType = vm.searchCondition.roomType;
      vm.search.startDate =
        vm.searchCondition.startDate || new Date().toISOString().substr(0, 10);
      vm.search.endDate =
        vm.searchCondition.endDate || new Date().toISOString().substr(0, 10);

      vm.isLoading = true;
      vm.axios
        .get("Hotel/all", {
          params: vm.search
        })
        .then(response => {
          vm.items = response.data.content;
          vm.pageLength = response.data.totalPages;
          console.log("i success");
        })
        .catch(error => {
          console.log(error);
          console.warn("Not good man :(");
        })
        .finally(function() {
          vm.isLoading = false;
        });
    },
    fakeData() {
      var sample = {
        content: [
          {
            createTime: "2019-11-16T08:50:40.533+0000",
            updateTime: "2019-11-16T08:50:40.533+0000",
            id: 3192,
            name: "����90",
            star: 2,
            locality: "����",
            address: "����楝銝挾38���",
            jsonFileId: 90,
            singleRoom: 10,
            singleRoomPrice: 1039.0,
            doubleRoom: 24,
            doubleRoomPrice: 1404.0,
            quadRoom: 15,
            quadRoomPrice: 2115.0
          },
          {
            createTime: "2019-11-16T08:50:55.642+0000",
            updateTime: "2019-11-16T08:50:55.642+0000",
            id: 3200,
            name: "����92",
            star: 2,
            locality: "����",
            address: "撱箏��楝2畾�64撌�9���",
            jsonFileId: 92,
            singleRoom: 27,
            singleRoomPrice: 1037.0,
            doubleRoom: 27,
            doubleRoomPrice: 1452.0,
            quadRoom: 14,
            quadRoomPrice: 2112.0
          },
          {
            createTime: "2019-11-16T08:51:03.189+0000",
            updateTime: "2019-11-16T08:51:03.189+0000",
            id: 3204,
            name: "����93",
            star: 2,
            locality: "����",
            address: "�����瘙��55撌�22���",
            jsonFileId: 93,
            singleRoom: 24,
            singleRoomPrice: 569.0,
            doubleRoom: 16,
            doubleRoomPrice: 1029.0,
            quadRoom: 11,
            quadRoomPrice: 1743.0
          },
          {
            createTime: "2019-11-16T08:52:03.579+0000",
            updateTime: "2019-11-16T08:52:03.579+0000",
            id: 3236,
            name: "����101",
            star: 2,
            locality: "����",
            address: "銝剖控��楝鈭挾57-1���",
            jsonFileId: 101,
            singleRoom: 16,
            singleRoomPrice: 967.0,
            doubleRoom: 29,
            doubleRoomPrice: 1711.0,
            quadRoom: 15,
            quadRoomPrice: 2621.0
          },
          {
            createTime: "2019-11-16T08:52:33.689+0000",
            updateTime: "2019-11-16T08:52:33.689+0000",
            id: 3252,
            name: "����105",
            star: 2,
            locality: "����",
            address: "�撅勗��敺瑁楝鈭挾367���",
            jsonFileId: 105,
            singleRoom: 25,
            singleRoomPrice: 882.0,
            doubleRoom: 15,
            doubleRoomPrice: 1275.0,
            quadRoom: 10,
            quadRoomPrice: 1861.0
          },
          {
            createTime: "2019-11-16T08:55:04.392+0000",
            updateTime: "2019-11-16T08:55:04.392+0000",
            id: 3332,
            name: "����125",
            star: 2,
            locality: "����",
            address: "�撅勗������12���",
            jsonFileId: 125,
            singleRoom: 22,
            singleRoomPrice: 695.0,
            doubleRoom: 24,
            doubleRoomPrice: 1412.0,
            quadRoom: 11,
            quadRoomPrice: 2265.0
          },
          {
            createTime: "2019-11-16T08:55:11.907+0000",
            updateTime: "2019-11-16T08:55:11.907+0000",
            id: 3336,
            name: "����126",
            star: 2,
            locality: "����",
            address: "銝剖控��楝鈭挾470撌�8���",
            jsonFileId: 126,
            singleRoom: 21,
            singleRoomPrice: 1058.0,
            doubleRoom: 26,
            doubleRoomPrice: 1482.0,
            quadRoom: 11,
            quadRoomPrice: 2032.0
          },
          {
            createTime: "2019-11-16T08:55:26.985+0000",
            updateTime: "2019-11-16T08:55:26.985+0000",
            id: 3344,
            name: "����128",
            star: 2,
            locality: "����",
            address: "�������楝82���",
            jsonFileId: 128,
            singleRoom: 14,
            singleRoomPrice: 709.0,
            doubleRoom: 24,
            doubleRoomPrice: 1433.0,
            quadRoom: 10,
            quadRoomPrice: 2390.0
          },
          {
            createTime: "2019-11-16T08:55:49.595+0000",
            updateTime: "2019-11-16T08:55:49.595+0000",
            id: 3356,
            name: "����131",
            star: 2,
            locality: "����",
            address: "����镼踹祐��楝36���10璅��3",
            jsonFileId: 131,
            singleRoom: 25,
            singleRoomPrice: 826.0,
            doubleRoom: 27,
            doubleRoomPrice: 1478.0,
            quadRoom: 11,
            quadRoomPrice: 2080.0
          },
          {
            createTime: "2019-11-16T08:56:42.439+0000",
            updateTime: "2019-11-16T08:56:42.439+0000",
            id: 3384,
            name: "����138",
            star: 2,
            locality: "����",
            address: "憭批�����銵�26���2璅�",
            jsonFileId: 138,
            singleRoom: 30,
            singleRoomPrice: 820.0,
            doubleRoom: 14,
            doubleRoomPrice: 1419.0,
            quadRoom: 13,
            quadRoomPrice: 2080.0
          }
        ],
        pageable: {
          sort: { sorted: false, unsorted: true, empty: true },
          offset: 10,
          pageNumber: 1,
          pageSize: 10,
          paged: true,
          unpaged: false
        },
        totalPages: 23,
        totalElements: 225,
        last: false,
        size: 10,
        number: 1,
        numberOfElements: 10,
        first: false,
        sort: { sorted: false, unsorted: true, empty: true },
        empty: false
      };
      this.items = sample.content;
      this.pageLength = sample.totalPages;
      this.isLoading = false;
    }
  },
  mounted: function() {
    // this.getHotelList();
    // this.fakeData();
    console.log("mounted");
  }
};
</script>