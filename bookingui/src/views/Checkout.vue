<template>
  <v-stepper v-model="e1">
    <v-stepper-header>
      <v-stepper-step :complete="e1 > 1" step="1">Confirm</v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step :complete="e1 > 2" step="2">Pay</v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step step="3">Complete</v-stepper-step>
    </v-stepper-header>

    <v-stepper-items>
      <v-stepper-content step="1">
        <v-progress-linear v-show="isLoading" indeterminate></v-progress-linear>
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
              <v-textarea outlined v-model="memo" color="teal">
                <template v-slot:label>
                  <div>Memo <small>(optional)</small></div>
                </template>
              </v-textarea>
            </v-col>
            <v-col cols="12" md="8">
              <HotelDetail
                v-if="order.rooms"
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
                v-if="order.rooms"
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
                v-if="order.rooms"
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
          <v-btn color="primary" @click="sendOrder" :disabled="isLoading">
            Send Order
          </v-btn>
          <v-btn class="ml-2" @click="$router.push('hotel')"
            >Back To Search</v-btn
          >
        </v-container>
      </v-stepper-content>
      <v-stepper-content step="2">
        <v-card class="checkout-content">
          <h1>order</h1>
          <h1>{{ order.name }}</h1>
          <h1>{{ order.star }}</h1>
          <h1>{{ order.locality }}</h1>
          <h1>{{ order.address }}</h1>
          <h1>Check-In: {{ order.StartDate }}</h1>
          <h1>Check-Out: {{ order.EndDate }}</h1>
          <h1>Total: {{ order.total }}</h1>
          <!-- <h1>{{ order.star }}</h1>
              <h1>{{ order.locality }}</h1>
              <h1>{{ order.address }}</h1>
              <h1>Check-In: {{ order.StartDate }}</h1>
              <h1>Check-Out: {{ order.EndDate }}</h1>
              <h1>Total: {{ Total }}</h1>
              <h1>Memo: {{ order.memo }}</h1> -->
          <v-alert dense outlined type="error" v-show="errorMsg != ''">
            Failed: <strong>{{ errorMsg }}</strong>
          </v-alert>
        </v-card>

        <v-btn color="primary" @click="pay">
          Submit Order
        </v-btn>
        <v-btn class="ml-2" @click="$router.push('order')">Pay Later</v-btn>
      </v-stepper-content>
      <v-stepper-content step="3">
        <v-card class="checkout-content" color="grey lighten-1">
          <h1>Complete.</h1>
        </v-card>
        <v-btn class="ml-2" @click="$router.push('hotel')"
          >Book Another Room</v-btn
        >
      </v-stepper-content>
    </v-stepper-items>
  </v-stepper>
</template>
<style scoped>
.checkout-content {
  height: calc(100vh - (212px));
}
</style>
<script>
import { mapGetters } from "vuex";
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
    },
    getOrder: {
      handler() {
        console.log("getOrder changed.");
      },
      deep: true
    }
  },
  // computed: mapState(["order"]),
  computed: {
    // mix the getters into computed with object spread operator
    ...mapGetters(["getOrder"])
  },
  data() {
    return {
      e1: 0,
      Total: 0,
      item: {},
      memo: "",
      isLoading: false,
      order: {},
      errorMsg: ""
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
      vm.isLoading = true;
      vm.axios
        .put("/Ordering/payOrder/" + vm.order.OrderId)
        .then(function(response) {
          vm.e1 = 3;
          console.log(response.data);
        })
        .catch(function(error) {
          console.log(error);
          console.log(error.response);
          vm.errorMsg = error.response.data.message;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    },
    sendOrder() {
      var vm = this;
      vm.isLoading = true;

      var roomTypeList = [];
      for (var roomType in vm.order.rooms) {
        var q = vm.order.rooms[roomType].Quantity;
        var list = vm.fillArray(roomType.replace("type", ""), q);
        roomTypeList.push(list.join(","));
      }
      vm.axios
        .post("/Ordering/add", {
          StartDate: vm.order.StartDate,
          EndDate: vm.order.EndDate,
          UserId: 8849,
          Memo: vm.memo,
          HotelRoomTypes: roomTypeList.join(","),
          HotelId: vm.item.jsonFileId
        })
        .then(function(response) {
          vm.e1 = 2;
          vm.order.OrderId = response.data.id;
          vm.order.total = response.data.total;
          console.log(response.data);
          
        })
        .catch(function(error) {
          console.log(error);
          vm.errorMsg = error.message;
        })
        .finally(function() {
          vm.isLoading = false;
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
          vm.order.name = vm.item.name;
          vm.order.star = vm.item.star;
          vm.order.locality = vm.item.locality;
          vm.order.address = vm.item.address;
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
    getOrderDetail: function() {
      var vm = this;
      vm.axios
        .get("Ordering/findMyOrders/" + vm.order.OrderId, {
          params: {
            userId: 8849
          }
        })
        .then(response => {
          console.log(response);
          vm.item = response.content;
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
    console.log(vm.order);
    vm.order.HotelId = vm.getOrder.HotelId;
    vm.order.StartDate = vm.getOrder.StartDate;
    vm.order.EndDate = vm.getOrder.EndDate;
    vm.order.rooms = vm.getOrder.rooms;

    if (vm.e1 <= 1 && vm.order.HotelId) {
      vm.getHotelDetail();
    } else if (vm.e1 <= 2 && vm.e1 > 1) {
      vm.getOrderDetail();
    }
  }
};
</script>