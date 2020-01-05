<template>
  <div>
    <OrderDetail
      :dialogControl="dialog"
      :selectedId="selectedId"
      @closeDialog="closeDialog"
    ></OrderDetail>
    <v-progress-linear v-show="isLoading" indeterminate></v-progress-linear>

    <v-tabs
      v-model="tab"
      class="elevation-2"
    >
      <v-tabs-slider></v-tabs-slider>

      <v-tab :key="1" :href="`#tab-1`">
        Incoming Orders
      </v-tab>
      <v-tab-item :key="'a'" :value="'tab-1'">
        <v-simple-table>
          <template v-slot:default>
            <thead>
              <tr>
                <th class="text-left">Name</th>
                <th class="text-left">Check-In</th>
                <th class="text-left">Check-Out</th>
                <th class="text-left">Stayed Nights</th>
                <th class="text-left">Booked Room(s)</th>
                <th class="text-left">Valid</th>
                <th class="text-left">Paid</th>
                <th class="text-left">Total</th>
                <th class="text-left"></th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in newOrderList" :key="item.id">
                <td>{{ item.name }}</td>
                <td>{{ $moment(item.startDate).format("YYYY-MM-DD") }}</td>
                <td>{{ $moment(item.endDate).format("YYYY-MM-DD") }}</td>
                <td>{{ ($moment(item.endDate)- $moment(item.startDate))/86400000}}</td>
                <td>{{ item.bookedQuantity }}</td>
                <td>{{ !item.isDisabled }}</td>
                <td v-if="item.isPaid">{{ item.isPaid }}</td>
                <td v-if="item.isPaid == false">
                  <v-btn
                    class="ma-2"
                    outlined
                    color="indigo"
                    :disabled="item.isDisabled"
                    @click="go2Pay(item.id)"
                    >Pay Now</v-btn
                  >
                </td>
                <td>{{ item.total }}</td>
                <td>
                  <v-btn
                    class="ma-2"
                    outlined
                    color="warning"
                    :disabled="item.isDisabled"
                    @click="changeOrder(item.id)"
                    >Change</v-btn
                  >
                </td>
              </tr>
            </tbody>
          </template>
        </v-simple-table>
      </v-tab-item>
      <v-tab :key="'b'" :href="`#tab-2`">
        Past Orders
      </v-tab>
      <v-tab-item :key="1" :value="'tab-2'">
         <v-simple-table>
          <template v-slot:default>
            <thead>
              <tr>
                <th class="text-left">Name</th>
                <th class="text-left">Check-In</th>
                <th class="text-left">Check-Out</th>
                <th class="text-left">Stayed Nights</th>
                <th class="text-left">Booked Room(s)</th>
                <th class="text-left">Valid</th>
                <th class="text-left">Paid</th>
                <th class="text-left">Total</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="item in oldOrderList" :key="item.id">
                <td>{{ item.name }}</td>
                <td>{{ $moment(item.startDate).format("YYYY-MM-DD") }}</td>
                <td>{{ $moment(item.endDate).format("YYYY-MM-DD") }}</td>
                <td>{{ ($moment(item.endDate)- $moment(item.startDate))/86400000}}</td>
                <td>{{ item.bookedQuantity }}</td>
                <td>{{ !item.isDisabled && item.isPaid }}</td>
                <td >{{ item.isPaid }}</td>
                <td>{{ item.total }}</td>
              </tr>
            </tbody>
          </template>
        </v-simple-table>
      </v-tab-item>
    </v-tabs>
  </div>
</template>

<script>
// @ is an alias to /src
import OrderDetail from "@/components/OrderDetail.vue";

export default {
  name: "order",
  components: {
    OrderDetail
  },
  data: () => ({
    tab: null,
    isLoading: false,
    newOrderList: [],
    oldOrderList: [],
    pageLength: [],
    selectedId: null,
    dialog: false,
    // selectedId: 9113
    msg: ""
  }),
  methods: {
    changeOrder(orderId) {
      var vm = this;
      vm.dialog = true;
      vm.selectedId = orderId;
    },
    closeDialog() {
      var vm = this;
      vm.dialog = false;
      vm.selectedId = null;
      vm.getIncomingOrder();
    },
    go2Pay(orderId) {
      this.$router.push({ path: "/checkout/2", query: { orderId: orderId } });
    },
    getPastOrder:function(){
      var params = {
          sortDesc: false,
          sortKey: "start_date",
          isPast:true
        }
      this.getOrderList(params, "oldOrderList")
    },
    getIncomingOrder:function(){
      var params = {
          sortDesc: false,
          sortKey: "start_date",
          isPast: false
        }
      this.getOrderList(params, "newOrderList")
    },
    getOrderList: function(reqParam, listName) {
      var vm = this;
      vm.isLoading = true;

      vm.axios
        .get("Ordering/findMyOrders", { params: reqParam })
        .then(response => {
          vm.$set(vm, listName, response.data.content);
        })
        .catch(error => {
          vm.msg = error.response.data.message;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    }
  },
  mounted: function() {
    // this.getOrderList();
    this.getPastOrder();
    this.getIncomingOrder();
    // this.dialog = true;
  }
};
</script>
