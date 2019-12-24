<template>
  <v-container fluid>
    <v-row justify="center">
      <v-col cols="11" md="5">
        <v-form ref="form" v-model="valid" :lazy-validation="lazy">
          <v-text-field
            v-model="account"
            :rules="emailRules"
            label="Account"
            required
          ></v-text-field>
          <v-text-field
            v-model="email"
            :rules="emailRules"
            label="E-mail"
            v-on:focus="EmailDefaultVal"
            required
          ></v-text-field>
          <v-text-field
            v-model="firstname"
            :counter="20"
            :rules="nameRules"
            label="First Name"
            required
          ></v-text-field>
          <v-text-field
            v-model="lastname"
            :counter="20"
            :rules="nameRules"
            label="Last Name"
            required
          ></v-text-field>

          <v-text-field
            v-model="password"
            :counter="20"
            :rules="passwordRules"
            label="Password"
            required
            type="password"
          ></v-text-field>

          <v-text-field
            v-model="pwdCheck"
            :counter="20"
            :rules="pwdCheckRules"
            label="Password Check"
            required
            type="password"
          ></v-text-field>

          <v-btn color="error" class="mr-4" @click="reset">
            Reset Form
          </v-btn>

          <v-btn color="primary" @click="AddUser" :disabled="isLoading">
            Submit
          </v-btn>

          <v-alert class="mt-2" type="error" v-show="ErrorMsg !== ''">
            {{ ErrorMsg }}
          </v-alert>
        </v-form>
      </v-col>
    </v-row>
  </v-container>
</template>

<script>
export default {
  data: () => ({
    isLoading: false,
    valid: true,
    account: "",
    firstname: "",
    lastname: "",
    password: "",
    pwdCheck: "",
    passwordRules: [
      v => !!v || "Password is required",
      v => (v && v.length <= 20) || "Password must be less than 20 characters",
      v => (v && v.length >= 6) || "Password must be more than 6 characters."
    ],
    pwdCheckRules: [
      v => !!v || "Password check is required",
      v => (v && v.length <= 20) || "Password must be less than 20 characters.",
      v => (v && v.length >= 6) || "Password must be more than 6 characters."
      //   v => (v && v === this.password) || "Pasword does not match."
    ],
    ErrorMsg: "",
    nameRules: [
      v => !!v || "Name is required",
      v => (v && v.length <= 20) || "Name must be less than 20 characters"
    ],
    email: "",
    emailRules: [
      v => !!v || "E-mail is required",
      v => /.+@.+\..+/.test(v) || "E-mail must be valid"
    ],
    lazy: false
  }),
  watch: {
    password() {
      var vm = this;
      var pwdCheckMatch = vm.pwdCheck === vm.password;

      const rule = v => (v && pwdCheckMatch) || "Pasword does not match.";
      vm.pwdCheckRules[3] = rule;
    },
    pwdCheck() {
      var vm = this;
      var pwdCheckMatch = vm.pwdCheck === vm.password;

      const rule = v => (v && pwdCheckMatch) || "Pasword does not match.";
      vm.pwdCheckRules[3] = rule;
    }
  },
  methods: {
    EmailDefaultVal() {
      if (this.email == null || this.email == "") {
        this.email = this.account;
      }
    },
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
    },
    AddUser() {
      var vm = this;

      vm.ErrorMsg = "";
      if (this.$refs.form.validate()) {
        vm.isLoading = true;
        vm.axios
          .post("User/add", {
            firstname: vm.firstname,
            lastname: vm.lastname,
            email: vm.email,
            account: vm.account,
            password: vm.password
          })
          .then(response => {
            vm.items = response.data.content;
            vm.pageLength = response.data.totalPages;
            console.log("i success");

            this.$router.push({
              path: "/Success",
              query: { message: "Account created successfully." }
            });
          })
          .catch(error => {
            console.log(error);
            console.warn("Not good man :(");
            vm.ErrorMsg = error;
          })
          .finally(function() {
            // always executed
            vm.isLoading = false;
          });
      } else {
        // do nothing
      }
    }
  }
};
</script>