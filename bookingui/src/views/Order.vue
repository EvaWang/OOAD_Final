<template>
  <div>
    <OrderDetail :dialogControl="dialog" :selectedId="selectedId" @closeDialog="closeDialog"></OrderDetail>
    <v-progress-linear v-show="isLoading" indeterminate></v-progress-linear>
    <v-simple-table>
      <template v-slot:default>
        <thead>
          <tr>
            <th class="text-left">Name</th>
            <th class="text-left">Check-In</th>
            <th class="text-left">Check-Out</th>
            <th class="text-left">Booked Room(s)</th>
            <th class="text-left">Valid</th>
            <th class="text-left">Paid</th>
            <th class="text-left">Total</th>
            <th class="text-left"></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="item in orderList" :key="item.id">
            <td>{{ item.name }}</td>
            <td>{{ $moment(item.startDate).format("YYYY-MM-DD") }}</td>
            <td>{{ $moment(item.endDate).format("YYYY-MM-DD") }}</td>
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
    isLoading: false,
    orderList: [],
    pageLength: [],
    selectedId: null,
    dialog:false,
    // selectedId: 9113
    msg:""
  }),
  methods: {
    changeOrder(orderId){
      var vm = this;
      vm.dialog = true;
      vm.selectedId = orderId;
    },
    closeDialog(){
      var vm = this;
      vm.dialog = false;
      vm.selectedId = null;
    },
    go2Pay(orderId) {
      this.$router.push({ path: "/checkout/2", query: { orderId: orderId } });
    },
    getOrderList: function() {
      var vm = this;
      vm.isLoading = true;

      vm.axios
        .get("Ordering/findMyOrders")
        .then(response => {
          vm.orderList = response.data.content;
          vm.pageLength = response.data.totalPages;
          // console.log("i success");
        })
        .catch(error => {
          // console.log(JSON.stringify(error));
          // console.warn("Not good man :(");
          vm.msg = error.response.data.message;
        })
        .finally(function() {
          vm.isLoading = false;
        });
    }
  },
  mounted: function() {
    this.getOrderList();
    // this.dialog = true;
  }
};
</script>
