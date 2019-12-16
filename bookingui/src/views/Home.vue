<template>
  <div class="home">
    <SearchPanel></SearchPanel>
  </div>
</template>

<script>
// @ is an alias to /src
import SearchPanel from '@/components/SearchPanel.vue'

export default {
  name: 'home',
  components: {
    SearchPanel
  }
}
</script>
