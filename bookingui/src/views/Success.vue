<template>
  <v-container class="about">
    <v-row justify="center">
      <v-col cols="auto">
        <v-alert outlined text type="success">
          {{ message }}
        </v-alert>
      </v-col>
    </v-row>
    <v-row justify="center">
      <v-col class="text-center" cols="10" md="4">
          <v-btn href="/" large color="primary">Back to Home</v-btn>
      </v-col>
    </v-row>
  </v-container>
</template>
<script>
export default {
  name: "Success",
  data: () => ({
    message: ""
  }),
  mounted() {
    this.message = this.$route.query.message;
  }
};
</script>