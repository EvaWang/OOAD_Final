<template>
  <v-row justify="center">
    <v-dialog
      v-model="dialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <v-card class="detail-window">
        <v-toolbar dark color="primary">
          <v-btn icon dark @click="closeDialog">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>Order Infomation</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn dark text @click="updateRoom">Update Room</v-btn>
            <v-btn dark text @click="closeDialog">Close</v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-container v-if="order" class="mb-12 detail-container">
          <v-row>
            <v-col cols="12" md="4">
              <v-list two-line subheader>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Order ID</v-list-item-subtitle>
                    <v-list-item-title>{{ order.id }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Hotel Name</v-list-item-subtitle>
                    <v-list-item-title>{{ order.name }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Check-In Date</v-list-item-subtitle>
                    <v-list-item-title>
                      {{ order.startDate }}
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Check-Out Date</v-list-item-subtitle>
                    <v-list-item-title>
                      {{ order.endDate }}
                      <v-btn
                        class="ml-4"
                        depressed
                        small
                        color="warning"
                        @click="updateDate"
                        >Change</v-btn
                      >
                      <!-- <v-row dense class="justify-space-between">
                        <v-col cols="auto">{{order.endDate}}</v-col>
                        <v-col cols="auto"><v-btn depressed small color="warning" @click="updateDate">Change</v-btn></v-col>
                      </v-row> -->
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Hotel Level</v-list-item-subtitle>
                    <v-list-item-title
                      ><v-rating :value="order.star"></v-rating>
                    </v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-title>Hotel Locality</v-list-item-title>
                    <v-list-item-subtitle>{{
                      order.locality
                    }}</v-list-item-subtitle>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Hotel Address</v-list-item-subtitle>
                    <v-list-item-title>{{ order.address }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-divider></v-divider>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Total</v-list-item-subtitle>
                    <v-list-item-title
                      >$ {{ order.total }} NTD</v-list-item-title
                    >
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Memo</v-list-item-subtitle>
                    <v-list-item-title>{{ order.memo }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
              <v-divider></v-divider>
            </v-col>
            <v-col cols="12" md="8">
              <!-- 這裡的 Quantity 是假的  -->
              <HotelDetail
                v-if="(ori_room['type1'] || {}).Quantity > 0"
                :title="'Single Room'"
                :imgPath="require('../assets/single.jpg')"
                :Price="(order.rooms['type1'] || {}).Price"
                :HotelId="(order.rooms['type1'] || {}).jsonFileId"
                :Quantity="1"
                :RoomType="1"
                :mode_OldOrder="true"
                @updateBooking="updateBooking"
                @restoreBooking="restoreBooking"
                :BookingQuantityDefault="(order.rooms['type1'] || {}).Quantity"
              ></HotelDetail>
              <HotelDetail
                v-if="(ori_room['type2'] || {}).Quantity > 0"
                :title="'Double Room'"
                :imgPath="require('../assets/double.jpg')"
                :Price="(order.rooms['type2'] || {}).Price"
                :HotelId="(order.rooms['type2'] || {}).jsonFileId"
                :Quantity="1"
                :RoomType="2"
                :mode_OldOrder="true"
                @updateBooking="updateBooking"
                @restoreBooking="restoreBooking"
                :BookingQuantityDefault="(order.rooms['type2'] || {}).Quantity"
              ></HotelDetail>
              <HotelDetail
                v-if="(ori_room['type4'] || {}).Quantity > 0"
                :title="'Quad Room'"
                :imgPath="require('../assets/quad.jpg')"
                :Price="(order.rooms['type4'] || {}).Price"
                :HotelId="(order.rooms['type4'] || {}).jsonFileId"
                :Quantity="1"
                :RoomType="4"
                :mode_OldOrder="true"
                @updateBooking="updateBooking"
                @restoreBooking="restoreBooking"
                :BookingQuantityDefault="(order.rooms['type4'] || {}).Quantity"
              ></HotelDetail>
              <!-- <v-btn class="ma-2" outlined color="warning" @click="restore">Restore</v-btn> -->
            </v-col>
          </v-row>
          <v-alert dense outlined type="error">{{errorMsg}}
          </v-alert>
        </v-container>
      </v-card>
    </v-dialog>
  </v-row>
</template>
<style scoped>
  .detail-window{
    height: 100vh;
  }

  .detail-container{
    height: calc(100vh - 136px);
    overflow: auto;
  }
</style>
<script>
import HotelDetail from "@/components/HotelDetail";

export default {
  name: "OrderDetail",
  components: {
    HotelDetail
  },
  props: {
    dialogControl: Boolean,
    selectedId: Number
  },
  data() {
    return {
      dialog: false,
      order: {},
      ori_room: {},
      errorMsg:""
    };
  },
  watch: {
    dialogControl() {
      this.dialog = this.dialogControl;
    },
    selectedId() {
      if (this.selectedId) {
        this.getOrderDetail();
      }
    }
  },
  methods: {
    fillArray(value, len) {
      var arr = [];
      for (var i = 0; i < len; i++) {
        arr.push(value);
      }
      return arr;
    },
    updateDate() {},
    updateRoom() {
      var vm = this;
      vm.isLoading = true;

      var roomTypeList = [];
      for (var roomType in vm.order.rooms) {
        var q = vm.order.rooms[roomType].Quantity;
        var list = vm.fillArray(roomType.replace("type", ""), q);
        roomTypeList.push(list.join(","));
      }

      vm.axios
        .post("/Ordering/updateByBooking/" + vm.selectedId, {
          HotelRoomTypes: roomTypeList.join(",")
        })
        .then(function(response) {
          vm.e1 = 2;
          vm.order.OrderId = response.data.id;
          vm.order.total = response.data.total;
          vm.errorMsg = "";
          vm.$store.commit("removeOrder");
        })
        .catch(function(error) {
          vm.errorMsg = error.response.data.message;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    },
    closeDialog() {
      var vm = this;
      vm.dialog = false;
      vm.ori_room = {};
      vm.$set(vm.order, "rooms", {});
      vm.$set(vm, "order", {});
      this.$emit("closeDialog");
    },
    restoreBooking(type) {
      var vm = this;
      var rooms = vm.order.rooms;
      rooms["type" + type] = rooms["type" + type] || {};
      rooms["type" + type].Quantity = vm.ori_room["type" + type].Quantity;
    },
    getOrderDetail: function() {
      var vm = this;
      vm.axios
        .get("Ordering/findMyOrderDetails", {
          params: {
            orderId: vm.selectedId
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
          vm.order.memo = item.memo;
          var itemList = response.data.content;
          vm.$set(vm.order, "rooms", {});
          for (var roomIndex in itemList) {
            var room = itemList[roomIndex];
            var detail = {
              Price: room.price,
              Quantity: room.bookedQuantity,
              jsonFileId: room.jsonFileId
            };
            vm.$set(vm.order.rooms, "type" + room.roomType, detail);
          }
          vm.ori_room = JSON.parse(JSON.stringify(vm.order.rooms));
        })
        .catch(error => {
          vm.msg = error.response.data.message;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    },
    updateBooking: function(val) {
      var vm = this;
      var rooms = vm.order.rooms;
      rooms["type" + val.RoomType] = rooms["type" + val.RoomType] || {};
      rooms["type" + val.RoomType].Quantity = val.Quantity;
    },
    updateOrder: function() {}
  },
  mounted: function() {
    if (this.selectedId) {
      this.getOrderDetail();
    }
  }
};
</script>