<template>
  <v-stepper v-model="e1">
    <v-stepper-header>
      <v-stepper-step :complete="e1 > 1" step="1">Confirm</v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step :complete="e1 > 2" step="2">Pay</v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step step="3">Complete</v-stepper-step>
    </v-stepper-header>
    <div class="mb-1" v-show="!isLoading"></div>
    <v-progress-linear v-show="isLoading" indeterminate></v-progress-linear>
    <v-stepper-items>
      <v-stepper-content step="1">
        <v-dialog v-model="dialog" persistent max-width="290">
          <LoginTable @signIn="signIn" :mode_popup="true"></LoginTable>
        </v-dialog>
        <v-container class="mb-12 checkout-content">
          <v-row>
            <v-col cols="12" md="4">
            <OrderInfo :order="order"></OrderInfo>
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
                :Price="order.singleRoomPrice"
                :HotelId="order.jsonFileId"
                :Quantity="order.singleRoom - order.bookedSingleRoom"
                :RoomType="1"
                @updateBooking="updateBooking"
                :BookingQuantityDefault="(order.rooms['type1'] || {}).Quantity"
              ></HotelDetail>
              <HotelDetail
                v-if="order.rooms"
                :title="'Double Room'"
                :imgPath="require('../assets/double.jpg')"
                :Price="order.doubleRoomPrice"
                :HotelId="order.jsonFileId"
                :Quantity="order.doubleRoom - order.bookedDoubleRoom"
                :RoomType="2"
                @updateBooking="updateBooking"
                :BookingQuantityDefault="(order.rooms['type2'] || {}).Quantity"
              ></HotelDetail>
              <HotelDetail
                v-if="order.rooms"
                :title="'Quad Room'"
                :imgPath="require('../assets/quad.jpg')"
                :Price="order.quadRoomPrice"
                :Quantity="order.quadRoom - order.bookedQuadRoom"
                :BookingQuantity="1"
                :HotelId="order.jsonFileId"
                :RoomType="4"
                @updateBooking="updateBooking"
                :BookingQuantityDefault="(order.rooms['type4'] || {}).Quantity"
              ></HotelDetail>
            </v-col>
          </v-row>
        </v-container>
        <v-btn color="primary" @click="sendOrder" :disabled="isLoading">
          Send Order
        </v-btn>
        <v-btn class="ml-2" @click="$router.push('/hotel')"
          >Back To Search</v-btn
        >
      </v-stepper-content>
      <v-stepper-content step="2">
        <v-card class="mb-12 checkout-content">
          <OrderInfo :order="order"></OrderInfo>
          <v-alert dense outlined type="error" v-show="errorMsg != ''">
            Failed: <strong>{{ errorMsg }}</strong>
          </v-alert>
        </v-card>
        <v-btn color="primary" @click="pay" :disabled="order.isDisabled">
          Pay
        </v-btn>
        <v-btn class="ml-2" @click="$router.push('/order')">{{
          order.isDisabled ? "My Orders" : "Pay Later"
        }}</v-btn>
      </v-stepper-content>
      <v-stepper-content step="3">
        <v-card class="mb-12 checkout-content">
          <h1>Complete.</h1>
        </v-card>
        <v-btn color="primary" class="ml-2" @click="$router.push('/hotel')"
          >Book Another Room</v-btn
        >
        <v-btn color="success" class="ml-2" @click="$router.push('/order')"
          >My Orders</v-btn
        >
      </v-stepper-content>
    </v-stepper-items>
  </v-stepper>
</template>
<style scoped>
.checkout-content {
  height: calc(100vh - (264px));
  overflow: auto;
}
</style>
<script>
import { mapGetters } from "vuex";
import HotelDetail from "@/components/HotelDetail";
import LoginTable from "@/components/LoginTable";
import OrderInfo from "@/components/OrderInfo";

export default {
  name: "checkout",
  components: {
    HotelDetail,
    LoginTable,
    OrderInfo
  },
  props: {
    step: { default: 0, type: Number }
  },
  watch: {
    getUserInfo: {
      handler() {
        var vm = this;
        vm.dialog = vm.getUserInfo.token == "";
      },
      deep: true
    }
  },
  computed: {
    ...mapGetters(["getOrder"]),
    ...mapGetters(["getUserInfo"]),
  },
  data() {
    return {
      e1: 0,
      memo: "",
      isLoading: false,
      order: {
        id: null,
        name:"",
        star: 0,
        address: "",
        locality:"",
        startDate:"",
        endDate:"",
        stayNights:0,
        total:0,
      },
      errorMsg: "",
      dialog: false
    };
  },
  methods: {
    calcStayNights(){
      var aDay = (1000*60*60*24);
      var nights = (new Date(this.order.endDate)-new Date(this.order.startDate))/aDay;
      this.order.stayNights = nights;
    },
    signIn() {
      this.dialog = false;
    },
    fillArray(value, len) {
      var arr = [];
      for (var i = 0; i < len; i++) {
        arr.push(value);
      }
      return arr;
    },
    calcTotal(){
      var vm = this;
      var total = 0
      for (var roomType in vm.order.rooms) {
        var q = vm.order.rooms[roomType].Quantity;
        var p = vm.order.rooms[roomType].Price;
        total = total+ p*q;
      }
      vm.order.total = total*vm.order.stayNights ;
    },
    pay() {
      var vm = this;
      vm.isLoading = true;
      vm.axios
        .put("/Ordering/payOrder/" + vm.order.OrderId)
        .then(function() {
          vm.e1 = 3;
          vm.errorMsg = "";
        })
        .catch(function(error) {
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
          StartDate: vm.order.startDate,
          EndDate: vm.order.endDate,
          Memo: vm.memo,
          HotelRoomTypes: roomTypeList.join(","),
          HotelId: vm.order.jsonFileId
        })
        .then(function(response) {
          vm.e1 = 2;
          vm.order.OrderId = response.data.id;
          vm.order.total = response.data.total;
          vm.errorMsg = "";
          vm.$store.commit("removeOrder");
        })
        .catch(function(error) {
          vm.errorMsg = error.message;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    },
    updateBooking(val) {
      this.$store.commit("updateOrderDetail", val);
      this.calcTotal();
    },
    getHotelDetail: function() {
      var vm = this;
      vm.isLoading = true;
      vm.axios
        .get("Hotel/findById", {
          params: {
            ids: vm.order.HotelId,
            startDate: vm.order.startDate,
            endDate: vm.order.endDate
          }
        })
        .then(response => {
          var item = response.data[0];
          vm.$set(vm.order, "name", item.name);
          vm.order.star = item.star;
          vm.order.locality = item.locality;
          vm.order.address = item.address;
          vm.order.jsonFileId = item.jsonFileId;
          vm.order.isDisabled = item.isDisabled;
          vm.order.singleRoom = item.singleRoom;
          vm.order.singleRoomPrice = item.singleRoomPrice;
          vm.order.doubleRoom = item.doubleRoom;
          vm.order.doubleRoomPrice = item.doubleRoomPrice;
          vm.order.quadRoom = item.quadRoom;
          vm.order.quadRoomPrice = item.quadRoomPrice;
          vm.order.bookedSingleRoom = item.bookedSingleRoom;
          vm.order.bookedDoubleRoom = item.bookedDoubleRoom;
          vm.order.bookedQuadRoom = item.bookedQuadRoom;
          vm.error = "";
        })
        .catch(error => {
          vm.error = error.response;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    },
    getOrderDetail: function() {
      var vm = this;
      vm.axios
        .get("Ordering/findMyOrders", {
          params: {
            orderId: vm.order.OrderId
          }
        })
        .then(response => {
          var item = response.data.content[0];
          vm.$set(vm.order, "name", item.name);
          vm.order.id = item.id;
          vm.order.star = item.star;
          vm.order.locality = item.locality;
          vm.order.address = item.address;
          vm.order.OrderId = item.id;
          vm.order.total = item.total;
          vm.order.startDate = vm.$moment(item.startDate).format("YYYY-MM-DD");
          vm.order.endDate = vm.$moment(item.endDate).format("YYYY-MM-DD");
          vm.order.isDisabled = item.isDisabled;
          vm.error = "";
        })
        .catch(error => {
          vm.error = error.response;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    }
  },
  mounted: function() {
    var vm = this;
    vm.dialog = vm.getUserInfo.token == "";
    vm.e1 = vm.$route.params.step;

    if (vm.e1 <= 1 && vm.getOrder.HotelId) {
      vm.$set(vm.order, "HotelId", vm.getOrder.HotelId);
      vm.order.startDate = vm.getOrder.startDate;
      vm.order.endDate = vm.getOrder.endDate;
      vm.order.rooms = vm.getOrder.rooms;
      vm.calcStayNights()
      vm.calcTotal(vm.order.rooms)
      vm.getHotelDetail();
    } else if (vm.e1 <= 2 && vm.e1 > 1) {
      vm.order.OrderId = vm.$route.query.orderId;
      vm.getOrderDetail();
    }
  }
};
</script>