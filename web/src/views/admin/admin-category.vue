<template>

  <a-layout>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-form
                layout="inline"
                :model="param"
        >
          <a-form-item>
            <a-input v-model:value="param.name" placeholder="名称">
            </a-input>
          </a-form-item>
          <a-form-item>
            <a-button type="primary"  @click="handleQuery({page : 1,size : pagination.pageSize})"  >
              查询
            </a-button>
          </a-form-item>

          <a-form-item>
            <a-button type="primary"  @click="add()"  >
              新增
            </a-button>
          </a-form-item>
        </a-form>
      </p>
      <a-table
              :columns="columns"
              :row-key="record => record.id"
              :data-source="categorys"
              :pagination="pagination"
              :loading="loading"
              @change="handleTableChange"
      >
        <template #cover="{ text: cover }">
          <img v-if="cover" :src="cover" alt="avatar" />
        </template>
        <template v-slot:action="{ text, record }">
          <a-space size="small">
            <a-button type="primary"  @click="edit(record)" >
                编辑
            </a-button>
            <a-popconfirm
                    title="删除后不可恢复，确认删除?"
                    ok-text="确认"
                    cancel-text="取消"
                    @confirm="handleDelete(record.id)"
            >
              <a-button type="danger">
                删除
              </a-button>
            </a-popconfirm>

          </a-space>
        </template>
      </a-table>
    </a-layout-content>
  </a-layout>
  <a-modal
          title="分类表单"
          v-model:visible="modalVisible"
          :confirm-loading="modalLoading"
          @ok="handleModalOk"
  >
    <a-form :model="category" :label-col="{span:6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="名称">
        <a-input v-model:value="category.name" />
      </a-form-item>

      <a-form-item label="父分类">
        <a-input v-model:value="category.parent" />
      </a-form-item>

      <a-form-item label="顺序">
        <a-input v-model:value="category.sort" />
      </a-form-item>

    </a-form>
  </a-modal>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref  } from 'vue';
  import axios from 'axios';
  import {message} from "ant-design-vue";
  import {Tool as tools} from "@/util/tool";


  export default defineComponent({
    name: 'AdminCategory',
    setup() {
      const param = ref();
      param.value = {};
      const categorys = ref();
      const pagination = ref({
        current: 1,
        pageSize:10,
        total: 0
      });
      const loading = ref(false);

      const columns = [



        {
          title: '名称',
          dataIndex: 'name'
        },{
          title: '父分类',
          key: 'parent',
          dataIndex: 'parent'
        },
        {
          title: '顺序',
          dataIndex: 'sort'
        },
        {
          title: 'Action',
          key: 'action',
          slots: { customRender: 'action' }
        }
      ];

      /**
       * 数据查询
       **/
      const handleQuery = (params: any) => {
        loading.value = true;
        // 如果不清空现有数据，则编辑保存重新加载数据后，再点编辑，则列表显示的还是编辑前的数据
        axios.get("/category/list", {
          params:{
            page : params.page,
            size : params.size,
            name : param.value.name
          }
        }).then((response) => {
          loading.value = false;
          const data = response.data;
          if(data.success){
            categorys.value = data.content.list;
            //重置分页按钮
            pagination.value.current = params.page;
            pagination.value.total = data.content.total;
          }else {
            message.error(data.message);
          }
        });
      };

      /**
       * 表格点击页码时触发
       */
      const handleTableChange = (pagination: any) => {
        console.log("看看自带的分页参数都有啥：" + pagination);
        handleQuery({
          page: pagination.current,
          size: pagination.pageSize
        });
      };

      const category = ref();
      const modalText = ref<string>('Content of the modal');
      const modalVisible = ref<boolean>(false);
      const modalLoading = ref<boolean>(false);
      const handleModalOk = () => {
        // modalText.value = 'The modal will be closed after two seconds';
        modalLoading.value = true;
        // setTimeout(() => {
        //   modalVisible.value = false;
        //   modalLoading.value = false;
        // }, 2000);

        axios.post("/category/save",category.value).then((response) => {
          modalLoading.value = false;//loading的效果只要后端有返回的时候就应该去除掉 而不是返回成功的时候再去除掉
          const data = response.data;
          if(data.success){
            modalVisible.value = false;
            handleQuery({
              page : pagination.value.current,
              size : pagination.value.pageSize,
            });
          }else{
              message.error(data.message);
          }
        });
      };
      /***
       * 编辑
       * @param record
       */
      const edit = (record : any) => {
        modalVisible.value = true;
        category.value = tools.copy(record);
      };
      /***
       * 新增
       */
      const add = () => {
        modalVisible.value = true;
        category.value = {};
      };

      /***
       * 删除
       */
      //const handleDelete = ( id : number ) => {
      const handleDelete = (id: number) => {
        console.log("删除ID "+id);
        axios.delete("/category/delete/"+id).then((response) => {
          const data = response.data;
          if(data.success){
            handleQuery({
              page : pagination.value.current,
              size : pagination.value.pageSize,
            });
          }
        });
      };

      onMounted(() => {
        handleQuery({
          page : 1,
          size : pagination.value.pageSize,
        });
      });


      return {
        categorys,
        pagination,
        columns,
        loading,
        handleTableChange,

        category,
        modalVisible,
        modalLoading,
        handleModalOk,

        edit,
        add,
        handleDelete,

        handleQuery,
        param,
      }
    }
  });
</script>
