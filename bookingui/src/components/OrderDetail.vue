<template>
  <v-row justify="center">
    <v-dialog
      v-model="dialog"
      fullscreen
      hide-overlay
      transition="dialog-bottom-transition"
    >
      <v-card>
        <v-toolbar dark color="primary">
          <v-btn icon dark @click="closeDialog">
            <v-icon>mdi-close</v-icon>
          </v-btn>
          <v-toolbar-title>Order Infomation</v-toolbar-title>
          <v-spacer></v-spacer>
          <v-toolbar-items>
            <v-btn dark text @click="closeDialog">Save</v-btn>
            <v-btn dark text @click="closeDialog">Cancel</v-btn>
          </v-toolbar-items>
        </v-toolbar>
        <v-container v-if="order" class="mb-12">
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
                    <v-list-item-title>{{
                      order.name
                    }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Check-In Date</v-list-item-subtitle>
                    <v-list-item-title>{{
                      order.startDate
                    }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Check-Out Date</v-list-item-subtitle>
                    <v-list-item-title>{{
                      order.endDate
                    }}</v-list-item-title>
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
                    <v-list-item-title>{{
                      order.address
                    }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-divider></v-divider>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Total</v-list-item-subtitle>
                    <v-list-item-title>$ {{
                      order.total
                    }} NTD</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
                <v-list-item>
                  <v-list-item-content>
                    <v-list-item-subtitle>Memo</v-list-item-subtitle>
                    <v-list-item-title>{{
                      order.memo
                    }}</v-list-item-title>
                  </v-list-item-content>
                </v-list-item>
              </v-list>
              <v-divider></v-divider>
            </v-col>
            <v-col cols="12" md="8">
              <!-- 這裡的 Quantity 是假的  -->
              <HotelDetail
                v-if="order.rooms"
                :title="'Single Room'"
                :imgPath="require('../assets/single.jpg')"
                :Price="(order.rooms['type1'] || {}).Price"
                :HotelId="(order.rooms['type1'] || {}).jsonFileId"
                :Quantity="1"
                :RoomType="1"
                :mode_OldOrder="true"
                @updateBooking="updateBooking"
                :BookingQuantityDefault="(order.rooms['type1'] || {}).Quantity"
              ></HotelDetail>
              <HotelDetail
                v-if="order.rooms"
                :title="'Double Room'"
                :imgPath="require('../assets/double.jpg')"
                :Price="(order.rooms['type2'] || {}).Price"
                :HotelId="(order.rooms['type2'] || {}).jsonFileId"
                :Quantity="1"
                :RoomType="2"
                :mode_OldOrder="true"
                @updateBooking="updateBooking"
                :BookingQuantityDefault="(order.rooms['type2'] || {}).Quantity"
              ></HotelDetail>
              <HotelDetail
                v-if="order.rooms"
                :title="'Quad Room'"
                :imgPath="require('../assets/quad.jpg')"
                :Price="(order.rooms['type4'] || {}).Price"
                :HotelId="(order.rooms['type4'] || {}).jsonFileId"
                :Quantity="1"
                :RoomType="4"
                :mode_OldOrder="true"
                @updateBooking="updateBooking"
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
      notifications: false,
      sound: true,
      widgets: false,
      order: {},
      ori_room:{}
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
    closeDialog() {
      var vm = this;
      vm.dialog = false;
      vm.ori_room = {};
      vm.$set(vm.order, "rooms", {});
      vm.$set(vm, "order", {});
      this.$emit("closeDialog");
    },
    restore(){
      var vm = this;
      vm.$set(vm.order, "rooms", vm.ori_room);
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
          for(var roomIndex in itemList){
            var room = itemList[roomIndex];
            var detail = {
              Price: room.price,
              Quantity: room.bookedQuantity,
              jsonFileId: room.jsonFileId,
            }
            vm.$set(vm.order.rooms, "type"+room.roomType, detail);
          }
          vm.ori_room = vm.order.rooms;
          // console.log("i success");
        })
        .catch(error => {
          // console.log(error);
          // console.warn("Not good man :(");
          vm.msg = error.response.data.message;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    },
    updateBooking: function(){
    // updateBooking: function(val){
      // console.log(val)
    },
    updateOrder: function(){

    }
  },
  mounted: function() {
    if(this.selectedId){
      this.getOrderDetail();
    }
  }
};
</script>