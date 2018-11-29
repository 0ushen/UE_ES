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
    
    // Event handlers for the 3 buttons in the search box form.
    $('#btnAdd').click(doSave);
    $('#btnSearch').click(getSearch);
    $('#btnListAll').click(getAll);
    
    // Ask the server to add a section into the db.
    function doSave() {
        
        var json = getJsonFromInput();
        
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
                .find(':input:not([type=submit]):not([type=checkbox])');
        inputs.each(function() {
            section[$(this).attr('id').replace('-d', '')] = $(this).val();
        });
        section["id"] = id;
        section["teacherId"] = teacherId;

        // Section object is sent to the server as json.
        var json = JSON.stringify(section);
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
        
        var idToDelete = id;
        // The section id is sent to the server.
        $.ajax({
            url: url,
            data: {Action: "doDelete", id: idToDelete},
            success: function() {
                console.log($('#updateForm #sectionName-d').val() + 
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
    
    /* Ask the server to return a section list based on some criteria 
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
    
    // Ask the server to return a list of all the section in the database.
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
                .find(':input:not([type=submit]):not([type=checkbox])');
        inputs.each(function() {
            section[$(this).attr('id')] = $(this).val();
        });
        
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

            $('#results-box').toggleClass('col-lg-12 col-lg-6');

            /* Load the details from the row the user clicked on in a form .
             * Data can be changed directly in the database via this 
             * interface. */

            $('#details-box').load('section_details.html', function(){

                /* Completing default form values with values from the row 
                 * clicked. */
                
                console.log(clickedRow.find('.teacherLastName').html());
                $('#sectionName-d').val(clickedRow.find('.sectionName').html());
                $('#description-d').val(clickedRow.find('.description').html());
                $('#teacherLastName-d').val(clickedRow.find('.teacherLastName').html());
                
                
                var id = clickedRow.find('.id').html();
                var teacherId = clickedRow.find('.teacherId').html();
                
                /* This update event send data in the details form to the 
                 * server.
                 * The server will then update this section details based on
                 * what the user typed in the form. */

                $('#btnUpdate').click(function() {doUpdate(id, teacherId);});

                /* This event will ask the server to delete the person in 
                 * the detail box from the database. */

                $('#btnDelete').click(function() {doDelete(id);});
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
    
    
    $('#teacherNameDropdownButton').click(function() {
        
        $.ajax({
            url: "PersonServlet", 
            data: {Action: "getAll"},
            success: buildDropdown,
            error: showAjaxError,
            dataType: "json"
        });
    });
    
    function buildDropdown(response) {
        
        var dropdown = $('#teacherNameDropdown');
        dropdown.html('');
        var htmlContent = '';
        $.each(response, function(){
            htmlContent += '<a class="dropdown-item" href="#" data-teacherid="' + this.teacherId + '">' + this.lastName + '</a>';
        });
        dropdown.html(htmlContent);
        
        $('.dropdown-item').click(function() {
            $('#teacherLastName').val($(this).html());
            console.log($(this).data('teacherid'));
            section["teacherId"] = $(this).data('teacherid');
            //console.log(section["teacherId"]);
        });
    }
});