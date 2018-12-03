$(document).ready(function () {
    
    // Servlet url.
    var url = "PersonServlet";
    // Representation of a person
    var person = {
        firstName: '',
        lastName: '',
        email: '',
        country: '',
        city: '',
        postalCode: '',
        dateOfBirth: '',
        address: '',
        isTeacher: ''
    };
    // Global variable used to know which event was called last.
    var lastEvent = '';
    
    // Event handlers for the 3 buttons in the search box form.
    $('#btnAdd').click(doSave);
    $('#btnSearch').click(getSearch);
    $('#btnListAll').click(getAll);
    
    // Ask the server to add a person into the db.
    function doSave() {
        
        // first name and last name are mandatories inputs.
        var fn = $('#firstName');
        var ln = $('#lastName');
        if(fn.val()) { 
            fn.removeClass('is-invalid');
        }
        else {
            fn.addClass('is-invalid');
            return;
        }
        if(ln.val()) {
            ln.removeClass('is-invalid');
        }
        else {
            ln.addClass('is-invalid');
            return;
        }
        
        var json = getJsonFromInput();
        
        $.ajax({
            url: url,
            data: {Action: "doSave", JSON: json},
            success: function() {
                console.log(person.firstName + " " + person.lastName +
                        " was succesfully added to the DB");
                
                refresh();
            },
            error: showAjaxError
        });
        
    }
    
    // Ask the server to do an update on a person via his id.
    function doUpdate(id) {
        // Create a person object from the input data.
        var inputs = $('#updateForm')
                .find(':input:not([type=submit]):not([type=checkbox])');
        inputs.each(function() {
            person[$(this).attr('id').replace('-d', '')] = $(this).val();
        });
        person["isTeacher"] = $('#updateForm #isTeacher-d').is(':checked');
        person["id"] = id;

        // Person object is sent to the server as json.
        var json = JSON.stringify(person);
        $.ajax({
            url: url,
            data: {Action: "doUpdate", JSON: json},
            success: function() {
                console.log(person["firstName"] + " " + person["lastName"] +
                        " was succesfully changed in the DB");
                refresh();
            },
            error: showAjaxError
        });
    }
    
    // Ask the server to delete a person via an id.
    function doDelete(id) {
        
        var idToDelete = id;
        // The person id is sent to the server.
        $.ajax({
            url: url,
            data: {Action: "doDelete", id: idToDelete},
            success: function() {
                console.log($('#updateForm #firstName-d').val() + " " 
                        + $('#updateForm #lastName-d').val() +
                        " was succesfully deleted from the DB");
                /* Empty the details box, hide it and then refresh the data 
                 * in the table */
                $('#details-box').html('');
                $('#results-box').toggleClass('col-lg-12 col-lg-6');
                refresh();
            },
            error: showAjaxError
        });
        
    }
    
    /* Ask the server to return a person list based on some criteria 
     * in the search form */
    function getSearch() {
        
        var json = getJsonFromInput();
        
        $.ajax({
            url: url, 
            data: {Action: "getSearch", JSON: json},
            success: buildTable,
            error: showAjaxError,
            dataType: "json"
        });
        
        lastEvent = "getSearch";
        
    }
    
    // Ask the server to return a list of all the person in the database.
    function getAll() {
        
        $.ajax({
            url: url, 
            data: {Action: "getAll"},
            success: buildTable,
            error: showAjaxError,
            dataType: "json"
        });
        
        lastEvent = "getAll";
        
    }
    
    // Refresh the table. Will be used after a change in the db.
    function refresh() {
        if (lastEvent === 'getAll')
            getAll();
        else if (lastEvent === 'getSearch')
            getSearch();
    }
    
    /* This function will build a person object based on all the info 
     * in the search form.
     * It will then return this object as a json string.*/
    function getJsonFromInput() {
        
        var inputs = $('#search')
                .find(':input:not([type=submit]):not([type=checkbox])');
        inputs.each(function() {
            person[$(this).attr('id')] = $(this).val();
        });
        person.isTeacher = $('#search #isTeacher').is(':checked');
        
        console.log(person);
        
        return JSON.stringify(person);
    }
    
    /* This function create an html table which will be used to display database
     * info. */
    function buildTable(response) {
        
        $('tbody').html('');
        
        /* For each person in the server response an html row will be created
         * and put into the htmlContent variable .*/
        var htmlContent, yesOrNo;
        $.each(response, function(){
            // The boolean value is converted to a string.
            yesOrNo = this.isTeacher ? 'YES' : 'NO';
            
            htmlContent += '<tr class="clickable-row" data-href="#">' +
                '<td class="id hide">' + this.id + '</td>' +
                '<td class="firstName">' + this.firstName + '</td>' +
                '<td class="lastName">' + this.lastName + '</td>' +
                '<td class="email">' + this.email + '</td>' +
                '<td class="country">' + this.country + '</td>' +
                '<td class="city">' + this.city + '</td>' +
                '<td class="postalCode">' + this.postalCode + '</td>' +
                '<td class="address">' + this.address + '</td>' +
                '<td class="dateOfBirth">' + this.dateOfBirth + '</td>' +
                '<td class="isTeacher">' + yesOrNo + '</td>' + 
                '</tr>';     
        });
        
        // Every row is put into the table body.
        $('tbody').html(htmlContent);

        /* If the user click on a row a detailed interface based on the data
         * from this row is created and shown. */
        $('.clickable-row').click(toggleDetailsBox);
    }
    
    /* This function will create&show or empty&hide a detail box. 
     * A detail box is a user interface which contains the data of a person
     * from the table. 
     * This person is the one from the row the user clicked on*/
    function toggleDetailsBox() {
        
        /* I have to save $(this) value into a variable or it will be out 
         * of scope. */
        var clickedRow = $(this);
        
        /* Condition to check if the detail box is empty or not.
         * If it is empty a new one is created from the data in the row the
         * user clicked on.
         * If it's not the html content in the detail box is emptied. */

        if($('#details-box').html() === ''){
            /* Results box is cut in half in order to show the detail box 
             * next to it on big screens */

            $('#results-box').toggleClass('col-lg-12 col-lg-6');

            /* Load the details from the row the user clicked on in a form .
             * Data can be changed directly in the database via this 
             * interface. */

            $('#details-box').load('person_details.html', function(){

                /* Completing default form values with values from the row 
                 * clicked.
                 */

                $('#firstName-d').val(clickedRow.find('.firstName').html());
                $('#lastName-d').val(clickedRow.find('.lastName').html());
                $('#country-d').val(clickedRow.find('.country').html());
                $('#city-d').val(clickedRow.find('.city').html());
                $('#postalCode-d').val(clickedRow.find('.postalCode').html());
                $('#address-d').val(clickedRow.find('.address').html());

                /* Date is shown as a string so i have to convert it into a Date
                 * object in order to match it with the calendar input.*/

                var dateString = clickedRow.find('.dateOfBirth').html();
                var dateSplit = dateString.split('-');
                var date = new Date(parseInt(dateSplit[0]),
                parseInt(dateSplit[1]) - 1, parseInt(dateSplit[2]) + 1);
                //Couldn't find how to set the value of the calendar in JQuery.
                document.getElementById('dateOfBirth-d').valueAsDate = date;

                $('#email-d').val(clickedRow.find('.email').html());

                // Check the box if the person is a teacher
                if(clickedRow.find('.isTeacher').html() === 'YES')
                    $('#isTeacher-d').prop('checked', true);
                else
                    $('#isTeacher-d').prop('checked', false);
                
                var id = clickedRow.find('.id').html();
                
                /* This update event send data in the details form to the 
                 * server.
                 * The server will then update this person details based on
                 * what the user typed in the form. */

                $('#btnUpdate').click(function() {doUpdate(id)});

                /* This event will ask the server to delete the person in 
                 * the detail box from the database. */

                $('#btnDelete').click(function() {doDelete(id)});
          });
        }
        else{
            /* If the user clicked on the table while there is a detail box,
             * the detail box html content is emptied and the table takes all
             * the width possible on large screens */
            $('#details-box').html('');
            $('#results-box').toggleClass('col-lg-12 col-lg-6');
        }
    }
    
    // Show a message in the console if the ajax request returns an error.
    function showAjaxError(error) {
        console.log("AJAX error in request : " + JSON.stringify(error, null, 2));
    }
});