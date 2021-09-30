<template>
  <v-card :disabled="loading">
    <v-card-title class="title pb-0 pt-1" v-text="appExists ? 'Current application' : 'New application'"></v-card-title>
    <v-alert
        class="mx-4"
        type="error"
        v-if = "errored"
    >An error occurred</v-alert>
    <template v-else>
      <v-alert
          class="mx-4"
          type="success"
          v-if = "currentApp.status == 'COMPLETED'"
      >Mortgage issued</v-alert>
      <v-alert
          class="mx-4"
          type="warning"
          v-if = "currentApp.status == 'PRESCORING_FAILED'"
      >Prescoring rejected</v-alert>
      <v-alert
          class="mx-4"
          type="warning"
          v-if = "currentApp.status == 'FULLSCORING_FAILED'"
      >Fullcoring failed</v-alert>

      <v-card-text v-if="canCreateNewApp">
        <v-btn @click="createNewApp()" class="primary">Create new application</v-btn>
      </v-card-text>
      <template v-else>
        <v-card-text>
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
          <v-btn v-if="appExists" @click="sendAppToCamunda()" class="primary" :loading="saving">Send</v-btn>
          <v-btn v-if="appExists" @click="deleteApp()" class="warning" :loading="saving">Cancel</v-btn>
        </v-card-actions>
      </template>
    </template>
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
                // allowed prefixes here http://www.rabbitmq.com/stomp.html#d
                // translated on server side by UserDestinationMessageHandler, DefaultUserDestinationResolver
                subscriptionApplicationStatusUpdate=stompObj.stompClient.subscribe("/user/queue/mortgage.application.status.update", (data) => {
                  const message = data.body;
                  console.log(message);
                  const obj = JSON.parse(message);
                  // if (obj.status === "COMPLETED") {
                  //
                  // }
                  this.currentApp = obj;
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
          },
          canCreateNewApp() {
            return ['COMPLETED', 'PRESCORING_FAILED', 'USER_CANCELED', 'FULLSCORING_FAILED'].includes(this.currentApp.status)
          }
        },
        methods: {
          createNewApp() {
            this.currentApp = factory();
            this.saveApp();
          },
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
          sendAppToCamunda() {
            axios.put('/api/mortgage-application/send/' + this.currentApp.id)
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
