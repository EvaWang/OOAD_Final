<template>
  <v-list two-line subheader>
    <v-list-item v-show="order.id">
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
    <v-list-item v-show="order.id">
      <v-list-item-content>
        <v-list-item-subtitle>Canceled</v-list-item-subtitle>
        <v-list-item-title>{{ order.isDisabled }}</v-list-item-title>
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
        </v-list-item-title>
      </v-list-item-content>
    </v-list-item>
    <v-list-item>
      <v-list-item-content>
        <v-list-item-subtitle>Stayed Nights</v-list-item-subtitle>
        <v-list-item-title>
          {{ order.stayNights }}
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
        <v-list-item-subtitle>{{ order.locality }}</v-list-item-subtitle>
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
        <v-list-item-title>$ {{ order.total }} NTD</v-list-item-title>
      </v-list-item-content>
    </v-list-item>
    <v-list-item v-show="order.id">
      <v-list-item-content>
        <v-list-item-subtitle>Memo</v-list-item-subtitle>
        <v-list-item-title>{{ order.memo }}</v-list-item-title>
      </v-list-item-content>
    </v-list-item>
  </v-list>
</template>
<script>
export default {
  name: "OrderInfo",
  props: {
    order: Object,
  }
};
</script>