<template>
  <v-card class="elevation-12">
    <v-toolbar color="primary" dark flat>
      <v-toolbar-title>Login form</v-toolbar-title>
      <v-spacer />
    </v-toolbar>
    <v-card-text>
      <v-form>
        <v-text-field
          label="Account"
          name="login"
          prepend-icon="mdi-face"
          type="text"
          v-model="username"
        />

        <v-text-field
          id="password"
          label="Password"
          name="password"
          prepend-icon="mdi-key"
          type="password"
          v-model="password"
        />
      </v-form>
    </v-card-text>
    <v-card-text>
      <v-alert dense outlined type="error" v-show="errorMsg != ''">
        {{ errorMsg }}
      </v-alert>
    </v-card-text>
    <v-card-actions>
      <v-spacer />
      <v-btn color="success" @click="createUser">Create Account</v-btn>
      <v-btn color="primary" @click="signIn">Login</v-btn>
    </v-card-actions>
    <v-progress-linear v-show="isLoading" indeterminate></v-progress-linear>
  </v-card>
</template>

<script>
export default {
  name: "LoginTable",
  props: {
    mode_popup: {
      default: false,
      type: Boolean
    },
    nextLink: {
      default: "/order",
      type: String
    }
  },
  data: () => ({
    username: "",
    password: "",
    isLoading: false,
    errorMsg: ""
  }),
  methods: {
    createUser: function() {
      var vm = this;
      vm.$router.push({
        path: "/newUser"
      });
    },
    signIn: function() {
      var vm = this;
      vm.isLoading = true;
      vm.axios
        .post("Auth/authenticate", {
          username: vm.username,
          password: vm.password
        })
        .then(response => {
          vm.$store.commit("userInfoChange", response.data);
          vm.$emit("signIn");
          if (!vm.mode_popup) {
            vm.$router.push({ path: vm.nextLink });
          }
        })
        .catch(error => {
          vm.errorMsg = error.response.data.message;
        })
        .finally(function() {
          // always executed
          vm.isLoading = false;
        });
    }
  }
};
</script>