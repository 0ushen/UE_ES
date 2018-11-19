$(document).ready(function () {
    
    var url = "PersonServlet";
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
    
    $('#btnListAll').click(function() {

        $.ajax({
            url: url, 
            data: {Action: "getAll"},
            success: function(response) {
                var htmlContent, YON, date, dateString;
                
                $.each(response, function(){
                    YON = this.isTeacher ? 'YES' : 'NO';
                    
                    dateString = this.dateOfBirth;
                    if(dateString === "")
                        date = "undefined";
                    else
                        date = new Date(dateString).toISOString().split('T')[0];
                    
                    htmlContent += '<tr class="clickable-row" data-href="#">' +
                        '<td class="firstName">' + this.firstName + '</td>' +
                        '<td class="lastName">' + this.lastName + '</td>' +
                        '<td class="email">' + this.email + '</td>' +
                        '<td class="country">' + this.country + '</td>' +
                        '<td class="city">' + this.city + '</td>' +
                        '<td class="postalCode">' + this.postalCode + '</td>' +
                        '<td class="address">' + this.address + '</td>' +
                        '<td class="dateOfBirth">' + 
                        date + '</td>' +
                        '<td class="isTeacher">' + YON + '</td>' + 
                        '</tr>';
                    buildTable(htmlContent);
                });
            },
            error: function(error) {
                console.log("AJAX error in request : " + 
                        JSON.stringify(error, null, 2));
            },
            dataType: "json"
        });

    });
    
    $('#btnAdd').click(function (){
        
        var fn = $('#firstName');
        fn.val() ? fn.removeClass('is-invalid') : fn.addClass('is-invalid') ;
        var ln = $('#lastName');
        ln.val() ? ln.removeClass('is-invalid') : ln.addClass('is-invalid') ;
        
        var inputs = $('#search')
                .find(':input:not([type=submit]):not([type=checkbox])');
        inputs.each(function() {
            person[$(this).attr('id')] = $(this).val();
        });
        person["isTeacher"] = $('#search #isTeacher').is(':checked');
        
        var json = JSON.stringify(person);
        $.ajax({
            url: url,
            data: {Action: "doSave", JSON: json},
            success: function() {
                console.log(person.firstName + " " + person.lastName +
                        " was succesfully added to the DB");
            },
            error: function(error) {
                console.log("AJAX error in request : " + 
                        JSON.stringify(error, null, 2));
            },
            dataType: "json"
        });
        
    });

    /* This function create an html table which will be used to display database
     * info. */

    function buildTable(htmlContent) {

        // Insert html content into the table.

        $('tbody').html(htmlContent);
        
        // Event handler for a click on a row.

        $('.clickable-row').click(function() {
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

                    /* Date is shown in a dd/mm/yyyy format so i have to convert
                     *  it to a yyyy-mm-dd format (javascript default) in order 
                     *  to get it accepted in the date type input */

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
                    
                });
            }
            else{
                $('#details-box').html('');
                $('#results-box').toggleClass('col-lg-12 col-lg-6');
            }
        });
    }
});