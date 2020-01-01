<template>
  <v-card class="mx-auto mt-1 mb-1" outlined v-show="Quantity > 0">
    <div class="d-flex">
      <v-avatar class="ma-3" size="120" tile>
        <v-img :src="imgPath"></v-img>
      </v-avatar>
      <div class="mx-auto">
        <v-card-title class="headline">{{ title }}</v-card-title>
        <v-card-subtitle v-show="Quantity < 5">
          Only {{ Quantity }} room(s) left.</v-card-subtitle
        >
        <v-card-subtitle v-show="Quantity >= 5"> Enough rooms.</v-card-subtitle>
      </div>
      <div class="mx-auto">
        <v-card-title class="text-right">$ {{ Price }} NTD</v-card-title>
        <v-divider class="mx-4"></v-divider>
        <v-item-group style="height:40px">
          <v-container>
            <v-row justify="center" dense>
              <v-col cols="3">
                <v-item
                  ><v-btn text large icon @click="addRoom(-1)">
                    <v-icon>mdi-minus</v-icon>
                  </v-btn>
                </v-item>
              </v-col>
              <v-col cols="3">
                <v-item>
                  <v-text-field
                    label="Room(s)"
                    placeholder="Placeholder"
                    outlined
                    type="number"
                    v-model="BookingQuantity"
                  >
                  </v-text-field>
                </v-item>
              </v-col>
              <v-col cols="3">
                <v-item>
                  <v-btn text large icon @click="addRoom(+1)">
                    <v-icon>mdi-plus</v-icon>
                  </v-btn>
                </v-item>
              </v-col>
              <v-col cols="3" v-if="showCheckout">
                <v-item>
                  <v-btn
                    text
                    icon
                    color="indigo"
                    @click="go2checkout"
                    :disabled="Quantity <= 0"
                  >
                    <v-icon>mdi-cart-arrow-down</v-icon>
                  </v-btn>
                </v-item>
              </v-col>
            </v-row>
          </v-container>
        </v-item-group>
      </div>
    </div>
  </v-card>
</template>

<script>
export default {
  props: {
    title: String,
    imgPath: String,
    Quantity: Number,
    Price: Number,
    HotelId: Number,
    RoomType: Number,
    bookedRoom: Number,
    StartDate: String,
    EndDate: String,
    showCheckout: Boolean,
    BookingQuantityDefault: {
      default: 0,
      type: Number
    }
  },
  data: () => ({
    BookingQuantity: 0
  }),
  methods: {
    add2Favourite() {
      var vm = this;
      var newRoom = {
        HotelId: vm.HotelId,
        RoomType: vm.RoomType,
        Price: vm.Price,
        StartDate: vm.StartDate,
        EndDate: vm.EndDate
      };
      vm.$store.commit("addCheckoutList", newRoom);
    },
    addRoom(val) {
      var vm = this;
      if (val < 0 && vm.BookingQuantity > 0) {
        vm.BookingQuantity = parseInt(vm.BookingQuantity) - 1;
      }
      if (val > 0) {
        vm.BookingQuantity = parseInt(vm.BookingQuantity) + 1;
      }
      vm.$emit("updateBooking", {
        RoomType: vm.RoomType,
        Quantity: vm.BookingQuantity
      });
    },
    go2checkout() {
      var vm = this;
      var newRoom = {
        HotelId: vm.HotelId,
        RoomType: vm.RoomType,
        Price: vm.Price,
        StartDate: vm.StartDate,
        EndDate: vm.EndDate,
        Quantity: vm.BookingQuantity
      };
      vm.$store.commit("updateOrder", newRoom);
      vm.$router.push({ path: "/checkout", params: { step: 1 } }).catch();
    }
  },
  mounted: function() {
    var vm = this;
    if (vm.BookingQuantityDefault > 0) {
      vm.BookingQuantity = vm.BookingQuantityDefault;
    }
  }
};
</script>