<template>
  <v-stepper class="checkout-container" v-model="e1">
    <v-stepper-header>
      <v-stepper-step :complete="e1 > 1" step="1">Confirm</v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step step="2">Complete</v-stepper-step>
    </v-stepper-header>

    <v-stepper-items>
      <v-stepper-content step="1">
        <v-container>
          <v-row>
            <v-col cols="12" md="4">
              <h1>{{ item.name }}</h1>
              <h1>{{ item.star }}</h1>
              <h1>{{ item.locality }}</h1>
              <h1>{{ item.address }}</h1>
              <h1>Check-In: {{ order.StartDate }}</h1>
              <h1>Check-Out: {{ order.EndDate }}</h1>
              <h1>Total: {{ Total }}</h1>
            </v-col>
            <v-col cols="12" md="8">
              <HotelDetail
                :title="'Single Room'"
                :imgPath="require('../assets/single.jpg')"
                :Price="item.singleRoomPrice"
                :HotelId="item.jsonFileId"
                :Quantity="item.singleRoom - item.bookedSingleRoom"
                :RoomType="1"
                @updateBooking="updateBooking"
                :BookingQuantityDefault="(order.rooms['type1'] || {}).Quantity"
              ></HotelDetail>
              <HotelDetail
                :title="'Double Room'"
                :imgPath="require('../assets/double.jpg')"
                :Price="item.doubleRoomPrice"
                :HotelId="item.jsonFileId"
                :Quantity="item.doubleRoom - item.bookedDoubleRoom"
                :RoomType="2"
                @updateBooking="updateBooking"
                :BookingQuantityDefault="(order.rooms['type2'] || {}).Quantity"
              ></HotelDetail>
              <HotelDetail
                :title="'Double Room'"
                :imgPath="require('../assets/quad.jpg')"
                :Price="item.quadRoomPrice"
                :Quantity="item.quadRoom - item.bookedQuadRoom"
                :BookingQuantity="1"
                :HotelId="item.jsonFileId"
                :RoomType="4"
                @updateBooking="updateBooking"
                :BookingQuantityDefault="(order.rooms['type4'] || {}).Quantity"
              ></HotelDetail>
            </v-col>
          </v-row>

          <v-btn color="primary" @click="pay">
            Pay
          </v-btn>
          <v-btn class="ml-2" @click="$router.push('hotel')"
            >Back To Search</v-btn
          >
        </v-container>
      </v-stepper-content>
      <v-stepper-content step="2">
        <v-card class="mb-12 checkout-content" color="grey lighten-1"></v-card>
        <v-btn color="primary" @click="pay">
          Submit Order
        </v-btn>
        <v-btn class="ml-2" @click="e1 = 1">Back</v-btn>
      </v-stepper-content>
    </v-stepper-items>
  </v-stepper>
</template>
<style scoped>
.checkout-container {
  padding-top: 64px;
}
.checkout-content {
  height: calc(100vh - (212px));
}
</style>
<script>
import { mapState } from "vuex";
import HotelDetail from "@/components/HotelDetail";

export default {
  name: "checkout",
  components: {
    HotelDetail
  },
  props: {
    step: { default: 0, type: Number }
  },
  watch: {
    step: function() {
      this.e1 = this.step;
    }
  },
  computed: mapState(["order"]),
  data() {
    return {
      e1: 0,
      Total: 0,
      item: {}
    };
  },
  methods: {
    fillArray(value, len) {
  var arr = [];
  for (var i = 0; i < len; i++) {
    arr.push(value);
  }
  return arr;
    },
    pay() {
      var vm = this;
      var roomTypeList = []
      for(var roomType in vm.order.rooms){
        var q = vm.order.rooms[roomType].Quantity;
        var list = vm.fillArray(roomType.replace("type",""),q)
        roomTypeList.push(list.join(','))
      }
      vm.axios
        .post("/Ordering/add", {
          StartDate: vm.order.StartDate,
          EndDate: vm.order.EndDate,
          UserId: 8849,
          Memo:"MEMO",
          HotelRoomTypes: roomTypeList.join(','),
          HotelId: vm.item.jsonFileId,
        })
        .then(function(response) {
          console.log(response);
        })
        .catch(function(error) {
          console.log(error);
        });
    },
    updateBooking(val) {
      console.log(val);
      this.$store.commit("updateOrderDetail", val);
    },
    getHotelDetail: function() {
      var vm = this;
      vm.axios
        .get("Hotel/findById", {
          params: {
            ids: vm.order.HotelId,
            startDate: vm.order.StartDate,
            endDate: vm.order.EndDate
          }
        })
        .then(response => {
          console.log(response);
          vm.item = response.data[0];
          // vm.pageLength = response.data.totalPages;
          console.log("i success");
        })
        .catch(error => {
          console.log(error);
          console.warn("Not good man :(");
        })
        .finally(function() {
          vm.isLoading = false;
        });
    }
  },
  mounted: function() {
    var vm = this;

    if (vm.order.HotelId) {
      vm.getHotelDetail();
    }
  }
};
</script>