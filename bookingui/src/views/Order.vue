<template>
  <div>
    <OrderDetail :dialogControl="dialog" @closeDialog="dialog = !dialog"></OrderDetail>

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
            <td>{{ item.isDisabled }}</td>
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
                @click="dialog = !dialog"
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
    dialog:false
  }),
  methods: {
    go2Pay(orderId) {
      this.$router.push({ path: "/checkout/2", query: { orderId: orderId } });
    },
    getOrderList: function() {
      var vm = this;
      vm.isLoading = true;

      vm.axios
        .get("Ordering/findMyOrders/8849")
        .then(response => {
          vm.orderList = response.data.content;
          vm.pageLength = response.data.totalPages;
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
    fakeData: function() {
      var sampleObj = {
        content: [
          {
            createTime: "2019-12-29T19:14:49.019+0000",
            updateTime: "2019-12-29T19:14:49.019+0000",
            id: 8971,
            isDisabled: false,
            isPaid: false,
            startDate: "2019-12-28T00:00:00.000+0000",
            endDate: "2019-12-28T00:00:00.000+0000",
            userId: 8849,
            name: "台北17",
            star: 2,
            locality: "台北",
            address: "大安區忠孝東路四段180號1樓",
            jsonFileId: 17,
            roomType: 0,
            bookedIsDisabled: false,
            bookedQuantity: 1,
            total: 0,
            discount: 1.0,
            memo: "MEMO"
          },
          {
            createTime: "2019-12-29T19:16:13.192+0000",
            updateTime: "2019-12-29T19:16:13.192+0000",
            id: 8974,
            isDisabled: false,
            isPaid: false,
            startDate: "2019-12-28T00:00:00.000+0000",
            endDate: "2019-12-28T00:00:00.000+0000",
            userId: 8849,
            name: "台北17",
            star: 2,
            locality: "台北",
            address: "大安區忠孝東路四段180號1樓",
            jsonFileId: 17,
            roomType: 0,
            bookedIsDisabled: false,
            bookedQuantity: 3,
            total: 0,
            discount: 1.0,
            memo: "MEMO"
          },
          {
            createTime: "2019-12-30T05:23:54.313+0000",
            updateTime: "2019-12-30T05:23:54.313+0000",
            id: 8981,
            isDisabled: false,
            isPaid: false,
            startDate: "2019-12-29T00:00:00.000+0000",
            endDate: "2019-12-29T00:00:00.000+0000",
            userId: 8849,
            name: "台北17",
            star: 2,
            locality: "台北",
            address: "大安區忠孝東路四段180號1樓",
            jsonFileId: 17,
            roomType: 0,
            bookedIsDisabled: false,
            bookedQuantity: 1,
            total: 0,
            discount: 1.0,
            memo: "MEMO"
          },
          {
            createTime: "2019-12-30T05:47:42.516+0000",
            updateTime: "2019-12-30T05:47:42.516+0000",
            id: 8984,
            isDisabled: false,
            isPaid: false,
            startDate: "2019-12-29T00:00:00.000+0000",
            endDate: "2019-12-29T00:00:00.000+0000",
            userId: 8849,
            name: "台北16",
            star: 3,
            locality: "台北",
            address: "大同區民生西路198號2樓",
            jsonFileId: 16,
            roomType: 0,
            bookedIsDisabled: false,
            bookedQuantity: 1,
            total: 0,
            discount: 1.0,
            memo: "MEMO"
          }
        ],
        pageable: {
          sort: {
            sorted: false,
            unsorted: true,
            empty: true
          },
          offset: 0,
          pageNumber: 0,
          pageSize: 2147483647,
          unpaged: false,
          paged: true
        },
        totalPages: 1,
        totalElements: 29,
        last: true,
        size: 2147483647,
        number: 0,
        first: true,
        numberOfElements: 29,
        sort: {
          sorted: false,
          unsorted: true,
          empty: true
        },
        empty: false
      };
      this.orderList = sampleObj.content;
    }
  },
  mounted: function() {
    // this.fakeData();
    this.getOrderList();
  }
};
</script>
