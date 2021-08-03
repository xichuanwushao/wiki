<template>

  <a-layout>
    <a-layout-content
            :style="{ background: '#fff', padding: '24px', margin: 0, minHeight: '280px' }"
    >
      <p>
        <a-button type="primary"  @click="add()" size="large" >
          新增
        </a-button>
      </p>
      <a-table
              :columns="columns"
              :row-key="record => record.id"
              :data-source="ebooks"
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
          title="电子书表单"
          v-model:visible="modalVisible"
          :confirm-loading="modalLoading"
          @ok="handleModalOk"
  >
    <a-form :model="ebook" :label-col="{span:6 }" :wrapper-col="{ span: 18 }">
      <a-form-item label="封面">
        <a-input v-model:value="ebook.cover" />
      </a-form-item>
      <a-form-item label="名称">
        <a-input v-model:value="ebook.name" />
      </a-form-item>
      <a-form-item label="分类一">
        <a-input v-model:value="ebook.category1Id" />
      </a-form-item>
      <a-form-item label="分类二">
        <a-input v-model:value="ebook.category2Id" />
      </a-form-item>
      <a-form-item label="描述">
        <a-input v-model:value="ebook.description"  type="textarea" />
      </a-form-item>

    </a-form>
  </a-modal>
</template>

<script lang="ts">
  import { defineComponent, onMounted, ref  } from 'vue';
  import axios from 'axios';
  import {message} from "ant-design-vue";

  export default defineComponent({
    name: 'AdminEbook',
    setup() {
      const ebooks = ref();
      const pagination = ref({
        current: 1,
        pageSize:10,
        total: 0
      });
      const loading = ref(false);

      const columns = [
        {
          title: '封面',
          dataIndex: 'cover',
          slots: { customRender: 'cover' }
        },
        {
          title: '名称',
          dataIndex: 'name'
        },
        {
          title: '分类一',
          key: 'category1Id',
          dataIndex: 'category1Id'
        },
        {
          title: '分类二',
          dataIndex: 'category2Id'
        },
        {
          title: '文档数',
          dataIndex: 'docCount'
        },
        {
          title: '阅读数',
          dataIndex: 'viewCount'
        },
        {
          title: '点赞数',
          dataIndex: 'voteCount'
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
        axios.get("/ebook/list", {
          params:{
            page : params.page,
            size : params.size,
          }
        }).then((response) => {
          loading.value = false;
          const data = response.data;
          if(data.success){
            ebooks.value = data.content.list;
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

      const ebook = ref();
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

        axios.post("/ebook/save",ebook.value).then((response) => {
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
        ebook.value = record;
      };
      /***
       * 新增
       */
      const add = () => {
        modalVisible.value = true;
        ebook.value = {};
      };

      /***
       * 删除
       */
      //const handleDelete = ( id : number ) => {
      const handleDelete = (id: number) => {
        console.log("删除ID "+id);
        axios.delete("/ebook/delete/"+id).then((response) => {
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
        ebooks,
        pagination,
        columns,
        loading,
        handleTableChange,

        ebook,
        modalVisible,
        modalLoading,
        handleModalOk,

        edit,
        add,
        handleDelete,
      }
    }
  });
</script>
