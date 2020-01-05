<template>
  <v-row justify="center">
    <v-dialog v-model="pickDate" persistent>
      <v-card>
        <v-card-title class="headline">Select New Date Range</v-card-title>
        <v-card-text>
          <v-row justify="center">
            <v-col cols="12" md="6">
              <v-menu
                ref="menu_start"
                v-model="menu_start"
                :close-on-content-click="false"
                :return-value.sync="order.startDate"
                transition="scale-transition"
                offset-y
                max-width="290px"
                min-width="290px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="order.startDate"
                    label="Check In"
                    prepend-icon="mdi-calendar"
                    readonly
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker
                  v-model="order.startDate"
                  :min="tomorrow"
                  :max="max_date"
                  no-title
                  scrollable
                >
                  <v-spacer></v-spacer>
                  <v-btn text color="primary" @click="menu_start = false"
                    >Cancel</v-btn
                  >
                  <v-btn
                    text
                    color="primary"
                    @click="$refs.menu_start.save(order.startDate)"
                    >OK</v-btn
                  >
                </v-date-picker>
              </v-menu>
            </v-col>
            <!-- <v-spacer></v-spacer> -->
            <v-col cols="12" md="6">
              <v-menu
                ref="menu_end"
                v-model="menu_end"
                :close-on-content-click="false"
                :return-value.sync="order.endDate"
                transition="scale-transition"
                offset-y
                max-width="290px"
                min-width="290px"
              >
                <template v-slot:activator="{ on }">
                  <v-text-field
                    v-model="order.endDate"
                    label="Check Out"
                    prepend-icon="mdi-calendar"
                    readonly
                    v-on="on"
                  ></v-text-field>
                </template>
                <v-date-picker
                  v-model="order.endDate"
                  :min="picker_end_min"
                  :max="max_date"
                  no-title
                  scrollable
                >
                  <v-spacer></v-spacer>
                  <v-btn text color="primary" @click="menu_end = false"
                    >Cancel</v-btn
                  >
                  <v-btn
                    text
                    color="primary"
                    @click="$refs.menu_end.save(order.endDate)"
                    >OK</v-btn
                  >
                </v-date-picker>
              </v-menu>
            </v-col>
          </v-row>
        </v-card-text>
        <v-card-actions>
          <v-spacer></v-spacer>
          <v-btn color="green darken-1" text @click="pickDate = false"
            >Cancel</v-btn
          >
          <v-btn color="green darken-1" text @click="updateDate">Submit</v-btn>
        </v-card-actions>
      </v-card>
    </v-dialog>

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
                        @click="changeDate"
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
              <v-alert dense outlined type="error">{{ errorMsg }}</v-alert>
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
        </v-container>
      </v-card>
    </v-dialog>
  </v-row>
</template>
<style scoped>
.detail-window {
  height: 100vh;
}

.detail-container {
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
      pickDate: false,
      order: {},
      ori_room: {},
      errorMsg: "",
      tomorrow: this.$moment().add(1, "day").format("YYYY-MM-DD"),
      menu_start: false,
      menu_end: false
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
  computed: {
    max_date() {
      var vm = this;
      return vm
        .$moment(vm.order.startDate)
        .add(1, "month")
        .format("YYYY-MM-DD");
    },
    picker_end_min() {
      var vm = this;
      return vm
        .$moment(vm.order.startDate)
        .add(1, "day")
        .format("YYYY-MM-DD");
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
    changeDate() {
      this.pickDate = true;
    },
    updateDate() {
      var vm = this;
      vm.pickDate = false;
      vm.isLoading = true;

      vm.axios
        .post("/Ordering/updateByDate/" + vm.selectedId, {
          StartDate: vm.order.startDate,
          EndDate: vm.order.endDate
        })
        .then(function() {
          // .then(function(response) {
          vm.errorMsg = "";
        })
        .catch(function(error) {
          vm.errorMsg = error.response.data.message;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    },
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
        .then(function() {
          // .then(function(response) {
          vm.errorMsg = "";
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
      vm.errorMsg = "";
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