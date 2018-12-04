$(document).ready(function () {
    
    /*SELECT ue.name, ue.code, ue.num_of_periods, ue.is_decisive, section.name, person.last_name
    FROM ue
    INNER JOIN section ON ue.section_id=section.section_id
    iNNER JOIN organized_ue ON organized_ue.ue_id=ue.ue_id
    INNER JOIN planning ON organized_ue.organized_ue_id=planning.organized_ue_id
    INNER JOIN person ON planning.person_id=person.person_id*/
    
    /*SELECT DISTINCT person.person_id
FROM ue
INNER JOIN organized_ue ON organized_ue.ue_id=ue.ue_id
INNER JOIN planning ON organized_ue.organized_ue_id=planning.organized_ue_id
INNER JOIN person ON planning.person_id=person.person_id
WHERE ue.ue_id = 1;*/
    
    // Servlet url.
    var url = "UEServlet";
    // Representation of a ue
    var ue = {
        id: '',
        ueName: '',
        sectionName: '',
        sectionId : '',
        code: '',
        nbrOfPeriods: '',
        description: '',
        isDecisive: '',
        listOfTeachers: ''
    };
    // Global variable used to know which event was called last.
    var lastEvent = '';
    // Global variable used to store the section id to update.
    var sectionIdToUpdate;
    
    // Event handlers for the 4 buttons in the search box form.
    $('#btnAdd').click(doSave);
    $('#btnSearch').click(getSearch);
    $('#btnListAll').click(getAll);
    $('#teacherNameDropdownButton').click(buildDropdownOfPersons);
    $('#sectionNameDropdownButton').click(buildDropdownOfSections);
    
    // Ask the server to add an ue into the db.
    function doSave() {
        
        /* Check if a section has been selected from the dropdown before 
         * we send the request to the server. 
         * An ue can only be created with a section_id representing its section,
         * and this value is hidden in each tag of the dropdown, so choosing one
         * section from the dropdown is mandatory. */
        if(ue["sectionId"] === '') {
            alert('please select a section from the dropdown if you want to add ' + 
                    'an ue');
            $('#sectionName').addClass('is-invalid');
            return;
        }
        
        // The ueName is a mandatory input.
        if(ue.ueName === '') {
            alert('please enter a name for the ue.');
            $('#ueName').addClass('is-invalid');
            return;
        }
        
        // Transforms inputs fields value into a ue object and then into json.
        var json = getJsonFromInput();
        
        // ue object is sent to the server as json.
        $.ajax({
            url: url,
            data: {Action: "doSave", JSON: json},
            success: function() {
                console.log(ue.ueName +
                        " was succesfully added to the DB");
                
                refresh();
            },
            error: showAjaxError
        });
        
    }
    
    // Ask the server to do an update on an ue via its id.
    function doUpdate(id, sectionId) {
        
        // Create a ue object from the input data.
        var inputs = $('#updateForm')
                .find(':input:not([type=submit]):not([type=checkbox])' + 
                ':not([type=button])');
        inputs.each(function() {
            ue[$(this).attr('id').replace('-d', '')] = $(this).val();
        });
        ue["id"] = id;
        ue["sectionId"] = sectionId;

        // ue object is sent to the server as json.
        var json = JSON.stringify(ue);
        $.ajax({
            url: url,
            data: {Action: "doUpdate", JSON: json},
            success: function() {
                console.log(ue.ueName +
                        " was succesfully changed in the DB");
                refresh();
            },
            error: showAjaxError
        });
    }
    
    // Ask the server to delete an ue via an id.
    function doDelete(id) {
        
        // The ue id is sent to the server.
        $.ajax({
            url: url,
            data: {Action: "doDelete", id: id},
            success: function() {
                console.log($('#updateForm #ueName-d').val() + 
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
    
    /* Ask the server to return an ue list based on some criteria 
     * in the search form */
    function getSearch() {
        
        // Transforms inputs fields value into an ue object and then into json.
        var json = getJsonFromInput();
        
        // ue object is sent to the server in json format.
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
            ue[$(this).attr('id')] = $(this).val();
        });
        ue.isDecisive = $('#search #isDecisive').is(':checked');
        
        console.log('ue object from inputs : ');
        console.log(ue);
        
        return JSON.stringify(ue);
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
                '<td class="ueName">' + this.ueName + '</td>' +
                '<td class="sectionName">' + this.sectionName + '</td>' +
                '<td class="code">' + this.code + '</td>' +
                '<td class="nbrOfPeriods">' + this.nbrOfPeriods + '</td>' +
                '<td class="description">' + this.description + '</td>' +
                '<td class="isDecisive">' + this.isDecisive + '</td>' + 
                '<td class="sectionId hide">' + this.sectionId + '</td>' +
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
            $('#results-box').toggleClass('col-lg-12 col-lg-6');

            /* Load the details from the row the user clicked on in a form .
             * Data can be changed directly in the database via this 
             * interface. */
            $('#details-box').load('ue_details.html', function(){

                /* Completing default form values with values from the row 
                 * clicked. */
                $('#ueName-d').val(clickedRow.find('.ueName').html());
                $('#sectionName-d').val(clickedRow.find('.sectionName').html());
                $('#code-d').val(clickedRow.find('.code').html());
                $('#nbrOfPeriods-d').val(clickedRow.find('.nbrOfPeriods').html());
                $('#description-d').val(clickedRow.find('.description').html());
                $('#isDecisive-d').val(clickedRow.find('.isDecisive').html());
                
                var id = clickedRow.find('.id').html();
                sectionIdToUpdate = clickedRow.find('.sectionId').html();
                
                /* This update event send data in the details form to the 
                 * server.
                 * The server will then update this section details based on
                 * what the user typed in the form. */
                $('#btnUpdate').click(function() {doUpdate(id, sectionIdToUpdate);});

                /* This event will ask the server to delete the section in 
                 * the detail box from the database. */
                $('#btnDelete').click(function() {doDelete(id);});
                
                $('#sectionNameDropdownButton-d').click(buildDropdownOfPersons);
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
    
    /* Create a dropdown menu next to a teacher name input field. 
     * This dropdown will contains all the persons in the person table.*/
    function buildDropdownOfPersons() {
        
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
                    
                    // Make sure the input is not in red now that it has a value.
                    input.removeClass('is-invalid');
                });
                
            },
            error: showAjaxError,
            dataType: "json"
        });
    }
    
    function buildDropdownOfSections() {
        
        /* Storing the div element containing the input and dropdown button into
         * a variable */
        var div = $(this).parent().parent();
        
        /* Ajax call to the server. In case of success it will create a dropdown menu
         * with all the persons stored in the person table in it. */
        $.ajax({
            url: "SectionServlet", 
            data: {Action: "getAll"},
            success: function(response) {
                
                // Storing the dropdown menu element into a variable.
                var dropdown = div.find('.sectionNameDropdown');
                
                //Make sure the dropdown is clean first.
                dropdown.html('');
                
                // Create the dropdown menu from the database person table.
                var htmlContent = '';
                $.each(response, function(){
                    htmlContent += '<a class="dropdown-item" data-sectionid="' +
                            this.id + '">' + this.sectionName + '</a>';
                });
                dropdown.html(htmlContent);
                
                //
                $('.dropdown-item').click(function() {
                    // Storing the input text field element into a variable.
                    var input = div.find('.sectionName');
                    // Teacher name selected in the dropdown is shown in input field.
                    input.val($(this).html());
                    
                    /* The id of the teacher selected in the dropdown is stored
                     * in the section object or in a variable if it's the dropdown
                     * from details box. */
                    if(dropdown.attr('id') === 'sectionNameDropdown')
                        ue.sectionId = $(this).data('sectionid');
                    else if(dropdown.attr('id') === 'sectionNameDropdown-d')
                        sectionIdToUpdate = $(this).data('sectionid');
                    
                    // Make sure the input is not in red now that it has a value.
                    input.removeClass('is-invalid');
                });
                
            },
            error: showAjaxError,
            dataType: "json"
        });
    }
});


