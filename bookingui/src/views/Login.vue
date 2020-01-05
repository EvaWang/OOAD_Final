<template>
  <v-content>
    <v-container fluid>
      <v-row align="center" justify="center">
        <v-col cols="12" sm="8" md="4">
          <LoginTable></LoginTable>
        </v-col>
      </v-row>
    </v-container>
  </v-content>
</template>

<script>
import LoginTable from "@/components/LoginTable";

export default {
  name: "Login",
  components: {
    LoginTable
  },
};
</script>