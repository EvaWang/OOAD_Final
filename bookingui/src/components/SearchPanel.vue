<template>
  <div class="search">
    <v-form ref="form" v-model="valid" lazy-validation>
      <v-row justify="center">
        <v-col cols="10" md="8">
          <v-autocomplete
            v-model="location"
            label="Location"
            :items="locationList"
            required
          ></v-autocomplete>
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="5" md="4">
          <v-combobox
            v-model="roomType"
            item-text="Name"
            :items="roomTypeList"
            label="Room Type"
          ></v-combobox>
        </v-col>
            <!-- :tick-labels="[0,1,2,3,4,5]" -->

        <v-col cols="4" md="4">
          <v-slider
            v-model="NumberOfCustomer"
            :max="roomType.Limit"
            step="1"
            ticks="always"
            tick-size="4"
          ></v-slider>
        </v-col>
        <v-col cols="1">
          {{NumberOfCustomer}}人
        </v-col>
      </v-row>
      <v-row justify="center">
        <v-col cols="2" md="8">
          <v-checkbox
            v-model="rating[0]"
            class="mx-2"
            label="1 star"
          ></v-checkbox>
        </v-col>
        <v-col cols="2" md="8">
          <v-checkbox
            v-model="rating[1]"
            class="mx-2"
            label="2 stars"
          ></v-checkbox>
        </v-col>
        <v-col cols="2" md="8">
          <v-checkbox
            v-model="rating[2]"
            class="mx-2"
            label="3 star"
          ></v-checkbox>
        </v-col>
        <v-col cols="2" md="8">
          <v-checkbox
            v-model="rating[3]"
            class="mx-2"
            label="4 star"
          ></v-checkbox>
        </v-col>
        <v-col cols="2" md="8">
          <v-checkbox
            v-model="rating[4]"
            class="mx-2"
            label="5 star"
          ></v-checkbox>
        </v-col>
        <!-- <v-col cols="11" md="8">
            <v-rating v-model="rating"></v-rating>
        </v-col> -->
      </v-row>
      <v-row justify="center">
        <v-col cols="auto">
          <v-date-picker
            v-model="picker_start"
            :min="today"
            :max="max_date"
          ></v-date-picker>
        </v-col>
        <v-col cols="auto">
          <v-date-picker
            v-model="picker_end"
            :min="picker_start"
            :max="max_date"
          ></v-date-picker>
        </v-col>
      </v-row>

      <v-row justify="end">
        <v-col cols="auto">
          <v-btn
            :disabled="!valid"
            color="success"
            class="mr-4"
            @click="validate"
          >
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
    picker_start: new Date().toISOString().substr(0, 10),
    picker_end: new Date().toISOString().substr(0, 10),
    today: new Date().toISOString().substr(0, 10),
    valid: true,
    NumberOfCustomer: 0,
    roomType: {"Name":"Single", "Limit":1},
    roomTypeList: [
      {"Name":"Single", "Limit":1},
      {"Name":"Double", "Limit":2},
      {"Name":"Quad", "Limit":4}],
    location: "",
    locationList: ["台北", "台中", "台南", "高雄", "花蓮", "台東", "宜蘭"],
    rating: []
  }),
  watch: {
    picker_start() {
      var vm = this;
      var endDate = new Date(vm.picker_end);
      var starDate = new Date(vm.picker_start);

      if (endDate < starDate) {
        vm.picker_end = vm.picker_start;
      }
    },
    rating(){
      var vm = this;
      console.log(vm.rating)
    }
  },
  computed: {
    max_date() {
      var vm = this;
      var starDate = new Date(vm.picker_start);
      var nextThreeMonth = new Date(
        starDate.getFullYear(),
        starDate.getMonth() + 3,
        starDate.getDate()
      );
      return nextThreeMonth.toISOString().substr(0, 10);
    }
  },
  methods: {
    validate() {
      if (this.$refs.form.validate()) {
        this.snackbar = true;
      }
    },
    reset() {
      this.$refs.form.reset();
    },
    resetValidation() {
      this.$refs.form.resetValidation();
    }
  }
};
</script>