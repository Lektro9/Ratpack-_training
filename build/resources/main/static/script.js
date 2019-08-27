new Vue({
  el: '#app',
  data() {
    return {
      selected: '',
      searchWord: '',
      users: [],
      newName: '',
      newEmail: '',
      newId: '',
      editTrig: '',
      editName: '', 
      editEmail: ''
    };

  },
  methods: {
    createUser() { 
      axios.post('http://localhost:5050/users/new', {
        id: this.newId,
        name: this.newName,
        email: this.newEmail
      })
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
    },

    updateUser(user) { //TODO: do not update the database if you do not notice any changes
      axios.put('http://localhost:5050/users/update', {
        id: user.id,
        name: this.editName,
        email: this.editEmail
      })
      .then(function (response) {
        console.log(response);
      })
      .catch(function (error) {
        console.log(error);
      });
      user.name = this.editName;
      user.email = this.editEmail;
      this.editName = '';
      this.editEmail = '';
      this.editTrig = '';
    },

    addUser() { //TODO: do not create a new User when id is already there
      let newUser = new Object({
        id: this.newId,
        name: this.newName,
        email: this.newEmail });

      this.users.push(newUser);
      this.createUser()
      this.newId = "";
      this.newName = "";
      this.newEmail = "";
    },

    fetchJson() { //TODO: maybe put this in the "mounted" area, so it fetches the json when creating the gui?
      this.users = [];
      axios.get('http://localhost:5050/users')
      .then(response => {
          let userData = response.data;
          console.log(userData);
          for (var i = 0; i < userData.length; i++) {
            let userInfo = {
            id: userData[i].id,
            name: userData[i].name,
            email: userData[i].email
            }
            this.users.push(userInfo)
          }
      })
    },

    trigEditTrig(user) {
      this.editTrig = user.id;
      this.editName = user.name;
      this.editEmail = user.email;
    }
  },

  computed: {     //TODO: also search through emails
    filteredByEmails() {
      let filter = new RegExp(this.selected, 'i');
      return this.users.filter(element => element.email.match(filter));
    },
    filteredBySearchBar() {
      let filter = new RegExp(this.searchWord, 'i');
      return this.filteredByEmails.filter(element => element.name.match(filter));
    },
  }

   });