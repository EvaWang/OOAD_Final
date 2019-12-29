<template>
  <v-stepper class="checkout-container" v-model="e1">
    <v-stepper-header>
      <!-- <v-stepper-step :complete="e1 > 1" step="1">Choose One</v-stepper-step>
      <v-divider></v-divider> -->
      <v-stepper-step :complete="e1 > 1" step="1">Confirm</v-stepper-step>
      <v-divider></v-divider>
      <v-stepper-step step="3">Complete</v-stepper-step>
    </v-stepper-header>

    <v-stepper-items>
      <v-stepper-content step="1">
        <HotelDetail
          :title="'Single Room'"
          :imgPath="require('../assets/single.jpg')"
          :Price="item.singleRoomPrice"
          :Quantity="item.singleRoom - item.bookedSingleRoom"
          :HotelId="item.jsonFileId"
          :RoomType="1"
          :StartDate="item.startDate"
          :EndDate="item.endDate"
        ></HotelDetail>
        <HotelDetail
          :title="'Double Room'"
          :imgPath="require('../assets/double.jpg')"
          :Price="item.doubleRoomPrice"
          :Quantity="item.doubleRoom - item.bookedDoubleRoom"
          :HotelId="item.jsonFileId"
          :RoomType="2"
          :StartDate="item.startDate"
          :EndDate="item.endDate"
        ></HotelDetail>
        <HotelDetail
          :title="'Double Room'"
          :imgPath="require('../assets/quad.jpg')"
          :Price="item.quadRoomPrice"
          :Quantity="item.quadRoom - item.bookedQuadRoom"
          :HotelId="item.jsonFileId"
          :RoomType="4"
          :StartDate="item.startDate"
          :EndDate="item.endDate"
        ></HotelDetail>
        <!-- <v-item-group class="checkout-content">
          <v-row>
            <v-col v-for="(value, name) in checkoutList" :key="name" cols="12" md="4">
              <v-item v-slot:default="{ active, toggle }">
                <v-card
                  :color="active ? 'primary' : ''"
                  class="d-flex align-center "
                  height="200"
                  @click="toggle"
                >
                <v-card-title>{{value}} {{name}}</v-card-title>
                  <v-scroll-y-transition>
                    <div
                      v-if="active"
                      class="display-3 flex-grow-1 text-center"
                    >
                      Active
                    </div>
                  </v-scroll-y-transition>
                </v-card>
              </v-item>
            </v-col>
          </v-row>
        </v-item-group> -->

        <v-btn color="primary" @click="e1 = 2">
          Continue
        </v-btn>
        <v-btn class="ml-2" @click="$router.push('hotel')"
          >Back To Search</v-btn
        >
      </v-stepper-content>

      <!-- <v-stepper-content step="2">
        <v-card class="mb-12 checkout-content" color="grey lighten-1"></v-card>
        <v-btn color="primary" @click="e1 = 3">
          Continue
        </v-btn>
        <v-btn class="ml-2" @click="e1 = 1">Back</v-btn>
      </v-stepper-content> -->

      <v-stepper-content step="3">
        <v-card class="mb-12 checkout-content" color="grey lighten-1"></v-card>
        <v-btn color="primary" @click="pay">
          Submit Order
        </v-btn>
        <v-btn class="ml-2" @click="e1 = 2">Back</v-btn>
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

export default {
  props: {
    step: { default: 0, type: Number }
  },
  watch:{
    step:function(){
      this.e1 = this.step
    }
  },
  computed: mapState(["checkoutList"]),
  data() {
    return {
      e1: 0,
      item:{}
    };
  },
  methods:{
    pay(){
      
    },
    getHotelDetail:function(idList){
      var vm = this;
      vm.axios
        .get("Hotel/findById", {
          params: {
            ids: idList,
            startDate: '2019-12-20',
            endDate: '2019-12-21',
          }
        })
        .then(response => {
          console.log(response)
          // vm.items = response.data.content;
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
  mounted: function(){
    var vm = this;
    var idList= [];
    // if(vm.checkoutList)
      console.log(vm.checkoutList);
    for (var item in vm.checkoutList) {
      console.log(vm.checkoutList[item]);
      idList.push(vm.checkoutList[item].HotelId)
    }

    if(idList.length>0){
      vm.getHotelDetail(idList)
    }    
  }
};
</script>