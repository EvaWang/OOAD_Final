<template>
  <div class="search">
    <v-form ref="form" lazy-validation>
      <v-row justify="center">
        <v-col cols="12">
          <v-text-field v-model="location" label="Location"></v-text-field>
        </v-col>
        <v-col cols="6">
          <v-combobox
            v-model="selectRoomType"
            item-text="Name"
            :items="selectRoomTypeList"
            label="Room Type"
          ></v-combobox>
        </v-col>
        <v-col cols="">
          <v-slider
            v-model="NumberOfCustomer"
            :max="selectRoomType.Limit"
            step="1"
            ticks="always"
            tick-size="4"
          ></v-slider>
        </v-col>
        <v-col cols="1"> {{ NumberOfCustomer }}人 </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="12" md="6">
          <v-menu
            v-if="picker_start!=''"
            ref="menu_start"
            v-model="menu_start"
            :close-on-content-click="false"
            :return-value.sync="picker_start"
            transition="scale-transition"
            offset-y
            max-width="290px"
            min-width="290px"
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                v-model="picker_start"
                label="Check In"
                prepend-icon="mdi-calendar"
                readonly
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
              v-model="picker_start"
              :min="today"
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
                @click="$refs.menu_start.save(picker_start)"
                >OK</v-btn
              >
            </v-date-picker>
          </v-menu>
        </v-col>
        <!-- <v-spacer></v-spacer> -->
        <v-col cols="12" md="6">
          <v-menu
            v-if="picker_end!=''"
            ref="menu_end"
            v-model="menu_end"
            :close-on-content-click="false"
            :return-value.sync="picker_end"
            transition="scale-transition"
            offset-y
            max-width="290px"
            min-width="290px"
          >
            <template v-slot:activator="{ on }">
              <v-text-field
                v-model="picker_end"
                label="Check Out"
                prepend-icon="mdi-calendar"
                readonly
                v-on="on"
              ></v-text-field>
            </template>
            <v-date-picker
              v-model="picker_end"
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
                @click="$refs.menu_end.save(picker_end)"
                >OK</v-btn
              >
            </v-date-picker>
          </v-menu>
        </v-col>
      </v-row>
      <v-row justify="start">
        <v-col cols="auto">
          <v-checkbox
            true-value="1"
            v-model="rating[0]"
            class="mx-2"
            label="1 star"
          ></v-checkbox>
        </v-col>
        <v-col cols="auto">
          <v-checkbox
            v-model="rating[1]"
            class="mx-2"
            label="2 stars"
          ></v-checkbox>
        </v-col>
        <v-col cols="auto">
          <v-checkbox
            v-model="rating[2]"
            class="mx-2"
            label="3 stars"
          ></v-checkbox>
        </v-col>
        <v-col cols="auto">
          <v-checkbox
            v-model="rating[3]"
            class="mx-2"
            label="4 stars"
          ></v-checkbox>
        </v-col>
        <v-col cols="auto">
          <v-checkbox
            v-model="rating[4]"
            class="mx-2"
            label="5 stars"
          ></v-checkbox>
        </v-col>
      </v-row>
      <v-row justify="end">
        <v-col cols="auto">
          <v-btn color="success" class="mr-4" @click="seach">
            Search
          </v-btn>
          <v-btn color="error" class="mr-4" @click="reset">
            Reset Form
          </v-btn></v-col
        >
        <v-col cols="0" md="2">
          <!-- 右邊空白 -->
        </v-col>
      </v-row>
    </v-form>
  </div>
</template>

<script>
export default {
  name: "search",
  components: {},
  data: () => ({
    menu_start: false,
    menu_end: false,
    picker_start: "",
    picker_end: "",
    today: "",
    NumberOfCustomer: 1,
    selectRoomType: { Name: "Single", Limit: 1 },
    selectRoomTypeList: [
      { Name: "Single", Limit: 1 },
      { Name: "Double", Limit: 2 },
      { Name: "Quad", Limit: 4 }
    ],
    location: null,
    locationList: ["台北", "台中", "台南", "高雄", "花蓮", "台東", "宜蘭"],
    rating: []
  }),
  watch: {
    picker_start() {
      var vm = this;
      var endDate = new Date(vm.picker_end).getTime();
      var starDate = new Date(vm.picker_start).getTime();

      if (endDate <= starDate) {
        vm.picker_end = vm.picker_end_min;
      }
    }
  },
  computed: {
    picker_end_min() {
      var vm = this;
      return vm.$moment(vm.picker_start).add(1, 'day').format("YYYY-MM-DD");
    },
    max_date() {
      var vm = this;
      return vm.$moment(vm.picker_start).add(1, 'month').format("YYYY-MM-DD");
    }
  },
  methods: {
    reset() {
      this.$refs.form.reset();
    },
    seach() {
      var vm = this;
      var condition = {
        locality: vm.location,
        stars: vm.rating,
        roomType: vm.selectRoomType.Limit,
        startDate: vm.picker_start,
        endDate: vm.picker_end
      };
      vm.$store.commit("searchConditionUpdate", condition);
      vm.$emit("searchClick");
    }
  },
  mounted:function(){
    this.picker_start = this.$moment().add(1, 'day').format("YYYY-MM-DD");
    this.picker_end = this.picker_end_min;
  }
};
</script>