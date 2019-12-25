<template>
  <v-card class="mx-auto mt-3" outlined v-show="Quantity > 0">
    <div class="d-flex">
      <v-avatar class="ma-3" size="160" tile>
        <v-img :src="imgPath"></v-img>
      </v-avatar>
      <div class="mx-auto ">
        <v-card-title class="headline">{{title}}</v-card-title>
        <v-card-subtitle v-show="Quantity < 5">
          Only {{ Quantity }} room(s) left.</v-card-subtitle
        >
        <v-card-subtitle v-show="Quantity >= 5">
          Enough rooms.</v-card-subtitle
        >
        <v-divider class="mx-4"></v-divider>
        <v-card-title>Price: $ {{Price}} NTD</v-card-title>
      </div>
      <div class="d-flex">
        <v-card-actions>
          <v-row>
            <v-col cols="12">
              <v-btn text icon color="pink">
                <v-icon>mdi-heart</v-icon>
              </v-btn>
            </v-col>
            <v-col cols="12">
              <v-btn text icon color="indigo">
                <v-icon>mdi-cart-arrow-down</v-icon>
              </v-btn>
            </v-col>
          </v-row>
        </v-card-actions>
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
  },
  data: () => ({
    //
  })
};
</script>