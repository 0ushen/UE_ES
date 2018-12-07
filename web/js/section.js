$(document).ready(function () {
    
    // Servlet url.
    var url = "SectionServlet";
    // Representation of a section
    var section = {
        id: '',
        sectionName: '',
        description: '',
        teacherLastName: '',
        teacherId: ''
    };
    // Global variable used to know which event was called last.
    var lastEvent = '';
    // Global variable used to store the teacher id to update.
    var teacherIdToUpdate;
    
    // Event handlers for the 4 buttons in the search box form.
    $('#btnAdd').click(doSave);
    $('#btnSearch').click(getSearch);
    $('#btnListAll').click(getAll);
    $('#teacherNameDropdownButton').click(buildDropdown);
    
    // Ask the server to add a section into the db.
    function doSave() {
        
        /* Check if a teacher has been selected from the dropdown before 
         * we send the request to the server. 
         * A section can only be created with a person_id representing its teacher,
         * and this value is hidden in each tag of the dropdown, so choosing one
         * teacher from the dropdown is mandatory.*/
        if(section["teacherId"] === '') {
            alert('please select a person from the dropdown if you want to add ' + 
                    'a section');
            $('#teacherLastName').addClass('is-invalid');
            return;
        }
        
        // Transforms inputs fields value into a section object and then into json.
        var json = getJsonFromInput();
        
        // Section object is sent to the server as json.
        $.ajax({
            url: url,
            data: {Action: "doSave", JSON: json},
            success: function() {
                console.log(section.sectionName +
                        " was succesfully added to the DB");
                
                refresh();
            },
            error: showAjaxError
        });
        
    }
    
    // Ask the server to do an update on a section via its id.
    function doUpdate(id, teacherId) {
        
        // Create a section object from the input data.
        var inputs = $('#updateForm')
                .find(':input:not([type=submit]):not([type=checkbox])' + 
                ':not([type=button])');
        inputs.each(function() {
            section[$(this).attr('id').replace('-d', '')] = $(this).val();
        });
        section["id"] = id;
        section["teacherId"] = teacherId;

        // Section object is sent to the server as json.
        var json = JSON.stringify(section);
        
        // Section object is sent to the server as json.
        $.ajax({
            url: url,
            data: {Action: "doUpdate", JSON: json},
            success: function() {
                console.log(section["sectionName"] +
                        " was succesfully changed in the DB");
                refresh();
            },
            error: showAjaxError
        });
    }
    
    // Ask the server to delete a section via an id.
    function doDelete(id) {
        
        // The section id is sent to the server.
        $.ajax({
            url: url,
            data: {Action: "doDelete", id: id},
            success: function() {
                console.log($('#updateForm #sectionName-d').val() + 
                        " was succesfully deleted from the DB");
                /* Empty the details box, hide it and then refresh the data 
                 * in the table */
                $('#details-box').html('');
                $('#results-box').toggleClass('col-xl-12 col-xl-6');
                refresh();
            },
            error: showAjaxError
        });
        
    }
    
    /* Ask the server to return a section list based on some criteria 
     * in the search form */
    function getSearch() {
        
        //Making sure at least one input is filled before searching in db.
        if(!$('#sectionName').val() && !$('#description') 
                && !$('#teacherLastName').val()) {
            $('#sectionName, #description, #teacherLastName')
                    .addClass('is-invalid');
            alert('At least one input has to be filled. Tray again.');
            return;
        }
        else {
            $('#sectionName, #description, #teacherLastName')
                    .removeClass('is-invalid');
        }
        
        // Transforms inputs fields value into a section object and then into json.
        var json = getJsonFromInput();
        
        // Section object is sent to the server as json.
        $.ajax({
            url: url, 
            data: {Action: "getSearch", JSON: json},
            success: buildTable,
            error: showAjaxError,
            dataType: "json"
        });
        
        lastEvent = "getSearch";
        
    }
    
    /* Ask the server to return a list of all the sections in the database. 
     * If the request succeed, the results are shown in a table.*/
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
    
    /* This function will build a section object based on all the info 
     * in the search form.
     * It will then return this object as a json string.*/
    function getJsonFromInput() {
        
        var inputs = $('#search')
                .find(':input:not([type=submit]):not([type=checkbox]):not([type=button])');
        inputs.each(function() {
            section[$(this).attr('id')] = $(this).val();
        });
        
        console.log('Section object from inputs : ');
        console.log(section);
        
        return JSON.stringify(section);
    }
    
    /* This function create an html table which will be used to display database
     * info. */
    function buildTable(response) {
        
        $('tbody').html('');
        
        /* For each section in the server response an html row will be created
         * and put into the htmlContent variable .*/
        var htmlContent;
        $.each(response, function(){
            htmlContent += '<tr class="clickable-row" data-href="#">' +
                '<td class="id hide">' + this.id + '</td>' +
                '<td class="sectionName">' + this.sectionName + '</td>' +
                '<td class="description">' + this.description + '</td>' +
                '<td class="teacherLastName">' + this.teacherLastName + '</td>' + 
                '<td class="teacherId hide">' + this.teacherId + '</td>' +
                '</tr>';     
        });
        
        // Every row is put into the table body.
        $('tbody').html(htmlContent);

        /* If the user click on a row a detailed interface based on the data
         * from this row is created and shown. */
        $('.clickable-row').click(toggleDetailsBox);
    }
    
    /* This function will create&show or empty&hide a detail box. 
     * A detail box is a user interface which contains the data of a section
     * from the table. 
     * This section is the one from the row the user clicked on. */
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
            $('#results-box').toggleClass('col-xl-12 col-xl-6');

            /* Load the details from the row the user clicked on in a form .
             * Data can be changed directly in the database via this 
             * interface. */
            $('#details-box').load('section_details.html', function(){

                /* Completing default form values with values from the row 
                 * clicked. */
                $('#sectionName-d').val(clickedRow.find('.sectionName').html());
                $('#description-d').val(clickedRow.find('.description').html());
                $('#teacherLastName-d').val(clickedRow.find('.teacherLastName').html());
                
                var id = clickedRow.find('.id').html();
                teacherIdToUpdate = clickedRow.find('.teacherId').html();
                
                /* This update event send data in the details form to the 
                 * server.
                 * The server will then update this section details based on
                 * what the user typed in the form. */
                $('#btnUpdate').click(function() {doUpdate(id, teacherIdToUpdate);});

                /* This event will ask the server to delete the section in 
                 * the detail box from the database. */
                $('#btnDelete').click(function() {doDelete(id);});
                
                $('#teacherNameDropdownButton-d').click(buildDropdown);
            });
        }
        else{
            /* If the user clicked on the table while there is a detail box,
             * the detail box html content is emptied and the table takes all
             * the width possible on large screens */
            $('#details-box').html('');
            $('#results-box').toggleClass('col-xl-12 col-xl-6');
        }
    }
    
    // Show a message in the console if the ajax request returns an error.
    function showAjaxError(error) {
        console.log("AJAX error in request : " + JSON.stringify(error, null, 2));
    }
    
    /* Create a dropdown menu next to a teacher name input field. 
     * This dropdown will contains all the persons in the person table.*/
    function buildDropdown() {
        
        /* Storing the div element containing the input and dropdown button into
         * a variable */
        var div = $(this).parent().parent();
        
        /* Ajax call to the server. In case of success it will create a dropdown menu
         * with all the persons stored in the person table in it. */
        $.ajax({
            url: "PersonServlet", 
            data: {Action: "getAll"},
            success: function(response) {
                
                // Storing the dropdown menu element into a variable.
                var dropdown = div.find('.teacherNameDropdown');
                
                //Make sure the dropdown is clean first.
                dropdown.html('');
                
                // Create the dropdown menu from the database person table.
                var htmlContent = '';
                $.each(response, function(){
                    htmlContent += '<a class="dropdown-item" data-teacherid="' +
                            this.id + '">' + this.lastName + '</a>';
                });
                dropdown.html(htmlContent);
                
                //
                $('.dropdown-item').click(function() {
                    // Storing the input text field element into a variable.
                    var input = div.find('.teacherLastName');
                    // Teacher name selected in the dropdown is shown in input field.
                    input.val($(this).html());
                    
                    /* The id of the teacher selected in the dropdown is stored
                     * in the section object or in a variable if it's the dropdown
                     * from details box. */
                    if(dropdown.attr('id') === 'teacherNameDropdown')
                        section["teacherId"] = $(this).data('teacherid');
                    else if(dropdown.attr('id') === 'teacherNameDropdown-d')
                        teacherIdToUpdate = $(this).data('teacherid');
                    
                    // Make sure the input is not in red now that it has a value.
                    input.removeClass('is-invalid');
                });
                
            },
            error: showAjaxError,
            dataType: "json"
        });
    }
});