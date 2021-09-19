<template>
  <v-card :disabled="loading">
    <v-card-title class="title pb-0 pt-1">Application</v-card-title>
    <v-row no-gutters>
      <v-col cols="12" >
        <v-row :align="'center'" no-gutters>
          <v-col class="mx-4">
            <v-text-field
                v-model="currentApp.property"
                :rules="[rules.min, rules.required]"
                label="Address"
                @keyup.native.enter="createApp()"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
    </v-row>

    <v-row no-gutters>
      <v-col cols="12" >
        <v-row :align="'center'" no-gutters>
          <v-col class="mx-4">
            <v-text-field
                v-model="currentApp.price"
                :rules="[rules.required]"
                label="Price"
                @keyup.native.enter="createApp()"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    <v-card-actions>
      <v-btn @click="createApp()" class="primary" :loading="saving">Save</v-btn>
    </v-card-actions>
  </v-card>

</template>

<script>

    import axios from "axios";
    import { mortgage_application_name} from "@/routes";

    const factory = () => {
        return {
          property: '',
          price: 0
        }
    }

    export default {
        data() {
            return {
              currentApp: factory(),
              loading: false,
              saving: false,
              rules: {
                required: value => !!value || 'Required.',
                min: v => v.length >= 4 || 'Min 4 characters',
              },

            }
        },
        mounted() {
            this.loading = true;
            axios.get('/api/mortgage-application').then(value => {
                let resp = value.data
                if (resp == {} || !resp) {
                  resp = factory();
                }
                this.currentApp = resp;
            }).finally(() => {
              this.loading = false;
            })
        },
        methods: {
          createApp() {
            this.saving = true;
            this.loading = true;
            axios.post('/api/mortgage-application', this.currentApp)
                .finally(() => {
              this.saving = false;
              this.loading = false;
            })

          }

        }
    }
</script>
