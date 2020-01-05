<template>
  <v-app>
    <v-app-bar app color="primary" dark>
      <div class="d-flex align-center">
        <v-img
          alt="Vuetify Logo"
          class="shrink mr-2"
          contain
          src="https://cdn.vuetifyjs.com/images/logos/vuetify-logo-dark.png"
          transition="scale-transition"
          width="40"
        />

        <v-btn
          @click="$router.push('/').catch(err => {})"
          text
          contain
          min-width="100"
          class="shrink mt-1 hidden-sm-and-down headline"
        >
          <span class="mr-2">Barking.com</span>
        </v-btn>
      </div>

      <v-spacer></v-spacer>
      <v-btn
        @click="$router.push('/hotel').catch(err => {})"
        text
      >
        <span class="mr-2">Search</span>
        <v-icon>mdi-magnify</v-icon>
      </v-btn>
      <v-btn
        text
        @click="
          $router
            .push({ path: '/checkout/1' })
            .catch(err => {})
        "
      >
        <span class="mr-2">Checkout</span>
        <v-badge color="red" right v-if="checkoutLen>0">
          <v-icon>mdi-shopping-outline</v-icon>
          <template v-slot:badge>
            <v-icon>mdi-bell-outline</v-icon>
          </template>
        </v-badge>
        <v-icon v-if="checkoutLen>0">mdi-shopping-outline</v-icon>
      </v-btn>
      <v-btn
        @click="$router.push('/order').catch(err => {})"
        text
        v-if="this.$store.state.userInfo.token!==''"
      >
        <span class="mr-2">My Order</span>
        <v-icon>mdi-hotel</v-icon>
      </v-btn>
      <v-menu offset-y v-if="this.$store.state.userInfo.token!==''">
        <template v-slot:activator="{ on }">
          <v-btn v-on="on" text>
            <span class="mr-2">{{getUserInfo.username}}</span>
            <v-icon>mdi-face</v-icon>
          </v-btn>
        </template>
        <v-list>
          <v-list-item>
            <v-btn text @click="signOut">
              <span class="mr-2">Sign Out</span>
              <v-icon>mdi-logout-variant</v-icon>
            </v-btn>
          </v-list-item>
        </v-list>
      </v-menu>
      <v-btn
        text
        v-if="this.$store.state.userInfo.token == ''"
        @click="$router.push('/login').catch(err => {})"
      >
        <span class="mr-2">Login</span>
        <v-icon>mdi-login-variant</v-icon>
      </v-btn>
    </v-app-bar>

    <div style="color:transparent; height:64px">Blank</div>
    <transition name="fade" mode="out-in">
      <router-view />
    </transition>
  </v-app>
</template>
<style scoped>
</style>
<script>
import { mapState, mapGetters } from "vuex";

export default {
  name: "App",
  computed: {
    ...mapGetters(["getUserInfo"]),
    ...mapState(["checkoutLen"])
  },
  data: () => ({
    // CheckoutLength: 0
  }),
  methods: {
    signIn: function() {
      this.$store.commit("userInfoChange", true);
    },
    signOut: function() {
      this.$store.commit("removeUserInfo");
      this.$router.push('/').catch()
    }
  }
};
</script>
