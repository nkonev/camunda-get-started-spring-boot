<template>
    <v-card>
      Mortgage application
      <v-btn v-if="!currentApp" color="primary">Create</v-btn>
    </v-card>

</template>

<script>

    import axios from "axios";

    export default {
        data() {
            return {
                currentApp: {}
            }
        },
        mounted() {
            axios.get('/api/mortgage-application').then(value => {
                this.currentApp = value.data;
            })
        }
    }
</script>
