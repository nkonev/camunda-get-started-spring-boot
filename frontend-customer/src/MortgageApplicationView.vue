<template>
  <v-card :disabled="loading">
    <v-card-title class="title pb-0 pt-1" v-text="appExists ? 'Current application' : 'New application'"></v-card-title>
    <v-alert
        class="mx-4"
        type="error"
        v-if = "errored"
    >An error occurred</v-alert>
    <v-card-text v-else>
    <v-row no-gutters>
      <v-col cols="12">
        <v-row :align="'center'" no-gutters>
          <v-col>
            <v-text-field
                v-model="currentApp.property"
                :rules="[rules.min, rules.required]"
                label="Address"
                @keyup.native.enter="saveApp()"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
    </v-row>

    <v-row no-gutters>
      <v-col cols="12" >
        <v-row :align="'center'" no-gutters>
          <v-col>
            <v-text-field
                v-model="currentApp.price"
                :rules="[rules.required]"
                label="Price"
                @keyup.native.enter="saveApp()"
            ></v-text-field>
          </v-col>
        </v-row>
      </v-col>
    </v-row>
    </v-card-text>
    <v-card-actions>
      <v-btn @click="saveApp()" class="primary" :loading="saving">Save</v-btn>
      <v-btn v-if="appExists" @click="deleteApp()" class="warning" :loading="saving">Cancel</v-btn>
    </v-card-actions>
  </v-card>

</template>

<script>
    import { initStompClient, closeStompClient } from './utils';
    import axios from "axios";

    let stompObj;
    let subscriptionApplicationStatusUpdate;

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
              errored: false
            }
        },
        mounted() {
            this.loading = true;
            this.errored = false;
            axios.get('/api/mortgage-application').then(value => {
                let resp = value.data
                if (!resp) {
                  resp = factory();
                }
                this.currentApp = resp;
            }).catch(reason => {
                this.errored = true;
            }).finally(() => {
              this.loading = false;
            });

          stompObj = initStompClient((frame) => {
                const url = stompObj.stompClient.ws._transport.url;
                // https://www.baeldung.com/spring-websockets-send-message-to-user
                console.log("Stomp transport url", url);
                // allowed prefixes here http://www.rabbitmq.com/stomp.html#d
                // translated on server side by UserDestinationMessageHandler, DefaultUserDestinationResolver
                subscriptionApplicationStatusUpdate=stompObj.stompClient.subscribe("/user/queue/mortgage.application.status.update", (data) => {
                  const message = data.body;
                  const obj = JSON.parse(message);
                  console.log(message);
                });
          });
        },
        beforeDestroy() {
          try {
            subscriptionApplicationStatusUpdate.unsubscribe();
          } catch (ignored){}
          
          closeStompClient(stompObj);
        },
        computed: {
          appExists() {
            return this.currentApp.id;
          }
        },
        methods: {
          saveApp() {
            this.saving = true;
            this.loading = true;

            let promise;
            if (this.currentApp.id) {
              promise = axios.patch('/api/mortgage-application', this.currentApp)
            } else {
              promise = axios.post('/api/mortgage-application', this.currentApp)
            }
            promise
                .then(value => {
                  this.currentApp = value.data;
                })
                .catch(reason => {
                  this.errored = true;
                })
                .finally(() => {
                  this.saving = false;
                  this.loading = false;
                })
          },
          deleteApp() {
            this.saving = true;
            this.loading = true;

            axios.delete('/api/mortgage-application/' + this.currentApp.id)
                .then(value => {
                  this.currentApp = factory();
                })
                .catch(reason => {
                  this.errored = true;
                })
                .finally(() => {
                  this.saving = false;
                  this.loading = false;
                })

          }
        }
    }
</script>
