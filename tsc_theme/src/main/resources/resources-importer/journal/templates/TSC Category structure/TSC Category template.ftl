<div class="row-fluid cat-page-container">
    <div class="span2">
        <!-- drop with category image -->
        <div class="cat-tear" style="background-color:${color.getData()}">
            <img alt="Image" src="${image.getData()}" />
            <!-- <i class="smart-cat_${stringUtil.valueOf(.vars['reserved-article-title'].data)?lower_case}"></i> -->
        </div>
    </div>
    <div class="span10">
        <h3 class="cat-page-title" style="color:${color.getData()}">${stringUtil.valueOf(.vars['reserved-article-title'].data)}</h3>
        <p>${description.getData()}</p>
    </div>
</div>
